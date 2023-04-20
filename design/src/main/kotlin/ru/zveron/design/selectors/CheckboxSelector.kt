package ru.zveron.design.selectors

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.gray2

@Composable
fun CheckboxSelector(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val color = animateColorAsState(
        targetValue = if (selected) MaterialTheme.colors.primary else gray2,
        animationSpec = tween(durationMillis = CheckboxAnimationDuration),
    )

    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = true,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = CheckboxButtonRippleRadius
                )
            )
            .size(CheckboxButtonSize)
            .background(color.value, RoundedCornerShape(4.dp))
    )
}

@Preview
@Composable
private fun SelectorPreview() {
    ZveronTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            CheckboxSelector(
                selected = true,
                onClick = {},
            )

            CheckboxSelector(
                selected = false,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun SelectorPreviewInteractive() {
    val isSelected = remember { mutableStateOf(true) }

    ZveronTheme {
        CheckboxSelector(
            selected = isSelected.value,
            onClick = { isSelected.value = !isSelected.value },
            modifier = Modifier.padding(10.dp)
        )
    }
}

private const val CheckboxAnimationDuration = 100
private val CheckboxButtonRippleRadius = 16.dp
private val CheckboxButtonSize = 16.dp