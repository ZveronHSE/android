package ru.zveron.application

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.zveron.authorization.fingerprint.FingerprintFactory

class ZveronApplication: Application() {
    private val fingerprintFactory by inject<FingerprintFactory>()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()

            androidContext(this@ZveronApplication)

            modules(appModule)
        }

        fingerprintFactory.getOrCreateFingerprint()
    }
}