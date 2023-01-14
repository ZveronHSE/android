package ru.zveron

import android.content.Context
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import ru.zveron.application.appModule

class AppModuleCheck {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAppModule() {
        appModule.verify(
            extraTypes = listOf(
                Context::class,
            )
        )
    }
}