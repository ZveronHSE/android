package ru.zveron.authorization.phone.password.deps

fun interface PasswordNavigator {
    fun navigateToRegistration(phone: String)
}