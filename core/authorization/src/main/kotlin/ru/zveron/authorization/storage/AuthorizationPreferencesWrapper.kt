package ru.zveron.authorization.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val PREFERENCES_FILE = "authorization_preferences"

private const val ACCESS_TOKEN_KEY = "access_token"
private const val ACCESS_EXPIRATION_KEY = "access_expiration"

private const val REFRESH_TOKEN_KEY = "refresh_token"
private const val REFRESH_EXPIRATION_KEY = "refresh_expiration"

private const val FINGERPRINT_TOKEN_KEY = "fingerprint"

internal class AuthorizationPreferencesWrapper(
    context: Context,
) {
    private val preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)

    var accessToken: String? by preferences.string(ACCESS_TOKEN_KEY)
    var accessTokenExpiration: Long? by preferences.long(ACCESS_EXPIRATION_KEY)

    var refreshToken: String? by preferences.string(REFRESH_TOKEN_KEY)
    var refreshTokenExpiration: Long? by preferences.long(REFRESH_EXPIRATION_KEY)

    var fingerprint: String? by preferences.string(FINGERPRINT_TOKEN_KEY)
}

private class PreferencesProperty<T>(
    private val name: String,
    private val defaultValue: T,
    private val getter: (String, T) -> T,
    private val setter: (String, T) -> Unit,
): ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return getter(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        setter(name, value)
    }
}

private fun SharedPreferences.string(name: String, default: String? = null) = PreferencesProperty(
    name,
    default,
    { varName, defaultValue -> getString(varName, defaultValue) },
    { varName, value ->
        edit { putString(varName, value) }
    }
)

private fun SharedPreferences.long(name: String, default: Long = 0L) = PreferencesProperty<Long?>(
    name,
    default,
    { varName, _ -> getLong(varName, default) },
    { varName, value ->
        edit {
            if (value != null) {
                putLong(varName, value)
            } else {
                remove(varName)
            }
        }
    }
)