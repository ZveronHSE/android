package ru.zveron.authorization.socials_sheet.data

import ru.zveron.authorization.model.Token

data class LoginBySocialResult(
    val accessToken: Token,
    val refreshToken: Token,
)
