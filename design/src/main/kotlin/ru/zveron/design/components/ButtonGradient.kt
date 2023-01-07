package ru.zveron.design.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zveron.design.theme.ZveronTheme

private val startColor = Color(0xFFE28213)
private val endColor = Color(0xFFFFBC37)

private val enabledButtonGradient = Brush.horizontalGradient(
    colors = listOf(startColor, endColor),
)

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val alpha = if (enabled) 1f else 0.5f

    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colors.onPrimary
    ) {
        Box(
            modifier = modifier
                .height(52.dp)
                .background(enabledButtonGradient, RoundedCornerShape(10.dp), alpha)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    enabled = enabled,
                    onClickLabel = onClickLabel,
                    role = Role.Button,
                    onClick = onClick,
                ),
            contentAlignment = contentAlignment,
            content = content,
        )
    }
}

@Composable
@Preview
fun PreviewGradient() {
    ZveronTheme {
        ActionButton(modifier = Modifier.fillMaxWidth()) {
            Text("Отправить код", style = MaterialTheme.typography.body1)
        }
    }
}