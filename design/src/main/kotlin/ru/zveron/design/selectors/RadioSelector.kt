package ru.zveron.design.selectors

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.enabledButtonGradient
import ru.zveron.design.theme.gray5

@Composable
fun RadioSelector(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = gray5,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val dotRadius = animateDpAsState(
        targetValue = if (selected) RadioButtonSize / 2 else 0.dp,
        animationSpec = tween(durationMillis = RadioAnimationDuration)
    )

    val selectableModifier =
            Modifier.selectable(
                selected = selected,
                onClick = onClick,
                enabled = true,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = RadioButtonRippleRadius
                )
            )

    Canvas(
        modifier
            .then(selectableModifier)
            .wrapContentSize(Alignment.Center)
            .requiredSize(RadioButtonSize)
    ) {
        if (dotRadius.value > 0.dp) {
            drawCircle(
                enabledButtonGradient,
                radius = dotRadius.value.toPx(),
                style = Fill,
            )
        }

        if (!selected) {
            drawCircle(
                color = borderColor,
                style = Stroke(1.dp.toPx()),
            )
        }
    }
}

@Preview
@Composable
private fun RadioSelectorPreview() {
    ZveronTheme {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(10.dp)) {
            RadioSelector(
                selected = true,
                onClick = {},
            )

            RadioSelector(
                selected = false,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun RadioSelectorPreviewInteractive() {
    val isSelected = remember { mutableStateOf(true) }

    ZveronTheme {
        RadioSelector(
            selected = isSelected.value,
            onClick = { isSelected.value = !isSelected.value },
            modifier = Modifier.padding(10.dp)
        )
    }
}

private const val RadioAnimationDuration = 100
private val RadioButtonRippleRadius = 16.dp
private val RadioButtonSize = 16.dp