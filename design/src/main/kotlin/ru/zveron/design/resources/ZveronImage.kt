package ru.zveron.design.resources

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter

@Immutable
sealed interface ZveronImage {
    @Immutable
    class ResourceImage(@DrawableRes val resId: Int): ZveronImage

    @Immutable
    class RemoteImage(val imageUrl: String): ZveronImage
}

@Composable
fun ZveronImage(
    zveronImage: ZveronImage,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    Image(
        painter = zveronImage.getPainter(),
        contentDescription = null,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}

@Composable
internal fun ZveronImage.getPainter(): Painter {
    return when (this) {
        is ZveronImage.RemoteImage -> rememberAsyncImagePainter(this.imageUrl)
        is ZveronImage.ResourceImage -> painterResource(id = this.resId)
    }
}