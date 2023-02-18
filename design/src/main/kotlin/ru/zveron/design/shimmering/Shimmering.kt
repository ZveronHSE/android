package ru.zveron.design.shimmering

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zveron.design.components.ActionButton
import ru.zveron.design.theme.ZveronTheme

fun Modifier.shimmeringBackground(
    width: Dp,
    shimmerWidth: Dp = 150.dp,
    shimmerColor: Color = Color.White.copy(alpha = 0.8f),
    baseColor: Color = Color.Transparent,
    cycleDurationMillis: Int = 1500,
): Modifier = composed {
    val transition = rememberInfiniteTransition()
    val position by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis = cycleDurationMillis))
    )
    val fullWidth = shimmerWidth * 2 + width
    val scale: Float = (shimmerWidth + width) / fullWidth
    val midOffset: Float = (shimmerWidth / 2) / fullWidth
    val rightOffset: Float = shimmerWidth / fullWidth
    val brush = with(LocalDensity.current) {
        Brush.horizontalGradient(
            colorStops = arrayOf(
                position * scale to baseColor,
                position * scale + midOffset to shimmerColor,
                position * scale + rightOffset to baseColor
            ),
            startX = -shimmerWidth.toPx(),
            endX = width.toPx() + shimmerWidth.toPx()
        )
    }

    this.then(
        Modifier.background(brush)
    )
}

@Preview
@Composable
fun PreviewGradientShimmering() {
    ZveronTheme {
        ActionButton(modifier = Modifier.fillMaxWidth()) {
            Text("Отправить код", style = MaterialTheme.typography.body1)

            Box(Modifier.fillMaxSize().shimmeringBackground(this.maxWidth))
        }
    }
}

@Preview
@Composable
internal fun CircleGradientShimmering() {
    ZveronTheme {
        BoxWithConstraints(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        ) {
            Box(Modifier.fillMaxSize().shimmeringBackground(this.maxWidth))
        }
    }
}
