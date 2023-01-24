package ru.zveron

import android.content.Context
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import ru.zveron.application.appModule
import ru.zveron.authorization.phone.password.deps.PasswordNavigator
import ru.zveron.authorization.phone.phone_input.deps.PhoneInputNavigator

class AppModuleCheck {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAppModule() {
        appModule.verify(
            extraTypes = listOf(
                Context::class,
                PasswordNavigator::class,
                PhoneInputNavigator::class,
                HttpLoggingInterceptor.Logger::class,
            )
        )
    }
}