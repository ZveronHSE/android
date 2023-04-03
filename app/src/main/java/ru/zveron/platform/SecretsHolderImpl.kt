package ru.zveron.platform

import ru.zveron.BuildConfig

class SecretsHolderImpl: SecretsHolder {
    override val googleClientId: String = BuildConfig.GOOGLE_CLIENT_ID
}