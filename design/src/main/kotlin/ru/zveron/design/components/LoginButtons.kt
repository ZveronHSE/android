package ru.zveron.design.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zveron.design.theme.GoogleColors
import ru.zveron.design.theme.LoginButtonColors
import ru.zveron.design.theme.VkColors

@Composable
fun LoginButton(
    loginButtonColors: LoginButtonColors,
    contentDescription: String? = null,
    onClick: () -> Unit = { },
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(45.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = loginButtonColors.backgroundColor),
        contentPadding = PaddingValues(),
    ) {
        Icon(
            painter = painterResource(loginButtonColors.iconRes),
            contentDescription = contentDescription,
            tint = loginButtonColors.foregroundColor,
        )
    }
}

@Preview
@Composable
fun CircularVkButton() {
    LoginButton(loginButtonColors = VkColors)
}

@Preview
@Composable
fun CircularGoogleButton() {
    LoginButton(loginButtonColors = GoogleColors)
}
