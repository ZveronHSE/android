package ru.zveron.authorization.socials_sheet

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import ru.zveron.authorization.domain.AuthorizationEventsEmitter
import ru.zveron.authorization.socials_sheet.domain.GoogleAuthInteractor
import ru.zveron.authorization.socials_sheet.domain.LoginBySocialsInteractor
import kotlin.coroutines.resumeWithException

class SocialsViewModel(
    private val authorizationService: AuthorizationService,
    private val googleAuthInteractor: GoogleAuthInteractor,
    private val loginBySocialsInteractor: LoginBySocialsInteractor,
    private val authorizationEventsEmitter: AuthorizationEventsEmitter,
    private val socialsModalBlocker: SocialsModalBlocker,
): ViewModel() {
    private val _startAuthFlow = MutableSharedFlow<Intent>(extraBufferCapacity = 1)
    val startAuthFlow = _startAuthFlow.asSharedFlow()

    private val _finishScreenFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val finishScreenFlow = _finishScreenFlow.asSharedFlow()

    val isLoading = mutableStateOf(false)

    fun googleSignInIntentClicked() {
        val intent = getGoogleSignInIntent()
        _startAuthFlow.tryEmit(intent)
    }

    fun onResult(intent: Intent) {
        socialsModalBlocker.blockModalBottomSheet()
        val response = AuthorizationResponse.fromIntent(intent)
        val error = AuthorizationException.fromIntent(intent)

        if (error != null) {
            socialsModalBlocker.unblockModalBottomSheet()
            Log.e("Authorization", "Error auth with google", error)
        } else if (response != null) {
            viewModelScope.launch {
                try {
                    isLoading.value = true

                    val token = requestToken(response.createTokenExchangeRequest())
                    Log.d("Authorization", "Login with token: $token")

                    loginBySocialsInteractor.loginWithGoogle(token)

                    isLoading.value = false

                    socialsModalBlocker.unblockModalBottomSheet()
                    _finishScreenFlow.tryEmit(Unit)
                    authorizationEventsEmitter.authorizationFinished(true)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    isLoading.value = false
                    socialsModalBlocker.unblockModalBottomSheet()
                    Log.e("Authorization", "Error auth with google", e)
                }
            }
        }
    }

    private suspend fun requestToken(tokenRequest: TokenRequest) = suspendCancellableCoroutine { continuation ->
        authorizationService.performTokenRequest(tokenRequest) { response, exception ->
            if (exception != null) {
                continuation.resumeWithException(exception)
            } else if (response != null) {
                continuation.resume(response.accessToken!!) {}
            }
        }
    }


    private fun getGoogleSignInIntent(): Intent {
        val request = googleAuthInteractor.getGoogleAuthorizationRequest()
        return authorizationService.getAuthorizationRequestIntent(request)
    }
}