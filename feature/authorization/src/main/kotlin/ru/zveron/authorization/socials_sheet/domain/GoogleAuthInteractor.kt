package ru.zveron.authorization.socials_sheet.domain

import android.net.Uri
import android.util.Base64
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import ru.zveron.platform.SecretsHolder
import java.security.MessageDigest
import java.security.SecureRandom

class GoogleAuthInteractor(
    private val secretsHolder: SecretsHolder,
) {
    private val authServiceConfig by lazy {
        AuthorizationServiceConfiguration(
            Uri.parse(GOOGLE_AUTH_URL),
            Uri.parse(GOOGLE_AUTH_TOKEN),
            null,
            null,
        )
    }

    private val secureRandom by lazy { SecureRandom() }

    fun getGoogleAuthorizationRequest(): AuthorizationRequest {
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)

        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        val codeVerifier = Base64.encodeToString(bytes, encoding)

        val digest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM)
        val hash = digest.digest(codeVerifier.toByteArray())
        val codeChallenge = Base64.encodeToString(hash, encoding)

        return AuthorizationRequest.Builder(
            authServiceConfig,
            secretsHolder.googleClientId,
            ResponseTypeValues.CODE,
            Uri.parse(GOOGLE_AUTH_REDIRECT),
        )
            .setCodeVerifier(codeVerifier, codeChallenge, CODE_VERIFIER_CHALLENGE_METHOD)
            .setScopes(
                "profile",
                "email",
                "openid",
            )
            .build()
    }

    companion object {
        private const val GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth"
        private const val GOOGLE_AUTH_TOKEN = "https://www.googleapis.com/oauth2/v4/token"
        private const val GOOGLE_AUTH_REDIRECT = "ru.zveron:/google_oauth2redirect"

        private const val CODE_VERIFIER_CHALLENGE_METHOD = "S256"
        private const val MESSAGE_DIGEST_ALGORITHM = "SHA-256"
    }
}