package ru.zveron.design.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.enabledButtonGradient

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentAlignment: Alignment = Alignment.Center,
    backgroundBrush: Brush = enabledButtonGradient,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    content: @Composable BoxWithConstraintsScope.() -> Unit = {},
) {
    val alpha = if (enabled) 1f else 0.5f

    CompositionLocalProvider(
        LocalContentColor provides contentColor
    ) {
        BoxWithConstraints(
            modifier = modifier
                .height(52.dp)
                .background(backgroundBrush, RoundedCornerShape(10.dp), alpha)
                .clip(RoundedCornerShape(10.dp))
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
fun RegularButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentAlignment: Alignment = Alignment.Center,
    brush: Brush = SolidColor(MaterialTheme.colors.surface),
    content: @Composable BoxWithConstraintsScope.() -> Unit = {},
) {
    val alpha = if (enabled) 1f else 0.5f

    BoxWithConstraints(
        modifier = modifier
            .height(36.dp)
            .background(brush, RoundedCornerShape(8.dp), alpha)
            .clip(RoundedCornerShape(8.dp))
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

@Composable
@Preview
fun PreviewGradient() {
    ZveronTheme {
        ActionButton(modifier = Modifier.fillMaxWidth()) {
            Text("Отправить код", style = MaterialTheme.typography.body1)
        }
    }
}

@Preview
@Composable
fun PreviewRegular() {
    RegularButton(modifier = Modifier.fillMaxWidth()) {
        Text("Отправить код", style = MaterialTheme.typography.body1)
    }
}