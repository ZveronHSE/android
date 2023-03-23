package ru.zveron.authorization.phone.phone_input.deps

interface PhoneInputNavigator {
    fun navigateToPasswordScreen()

    fun navigateToSmsScreen(phone: String, sessionId: String)
}