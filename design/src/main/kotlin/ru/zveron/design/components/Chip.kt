package ru.zveron.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.R
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.shimmering.shimmeringBackground
import ru.zveron.design.theme.enabledButtonGradient
import ru.zveron.design.theme.gray1
import ru.zveron.design.theme.gray5

@Composable
fun LoadingChip(
    width: Dp = 120.dp,
    height: Dp = 36.dp,
) {
    BoxWithConstraints(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .shimmeringBackground(this.maxWidth)
        )
    }
}

@Composable
fun Chip(
    title: ZveronText,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    onClick: () -> Unit = {},
    textMaxWidth: Dp = Dp.Unspecified,
    leadSlot: (@Composable () -> Unit)? = null,
    trailSlot: (@Composable () -> Unit)? = null,
) {
    val backgroundModifier = if (isActive) {
        Modifier.background(enabledButtonGradient)
    } else {
        Modifier.background(MaterialTheme.colors.surface)
    }

    val borderModifier = if (isActive) {
        Modifier
    } else {
        Modifier.border(1.dp, enabledButtonGradient, RoundedCornerShape(16.dp))
    }

    val textColor = if (isActive) gray1 else gray5

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                enabled = true,
                onClickLabel = title.getText(),
                role = Role.Button,
                onClick = onClick,
            )
            .then(backgroundModifier)
            .then(borderModifier)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        leadSlot?.let { 
            leadSlot.invoke()
        }

        ZveronText(
            text = title,
            color = textColor,
            style = TextStyle(
                fontSize = 14.sp,
                letterSpacing = (-0.24).sp,
                fontWeight = FontWeight.Normal,
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.sizeIn(maxWidth = textMaxWidth),
        )

        trailSlot?.let {
            trailSlot.invoke()
        }
    }
}

@Preview
@Composable
fun ChipActivePreview() {
    val textColor = gray1
    Chip(
        ZveronText.RawString("Порода"),
        isActive = true,
        leadSlot = {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null,
                tint = textColor,
            )
        },
        trailSlot = {
            Icon(
                painter = painterResource(R.drawable.ic_down_triangle),
                contentDescription = null,
                tint = textColor,
            )
        },
    )
}

@Preview
@Composable
fun ChipWithoutSlotsPreview() {
    Chip(
        ZveronText.RawString("Порода"),
        isActive = true,
    )
}

@Preview
@Composable
fun ChipNotActivePreview() {
    Chip(
        ZveronText.RawString("Порода")
    )
}