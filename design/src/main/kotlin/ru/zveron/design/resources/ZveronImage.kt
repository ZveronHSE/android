package ru.zveron.design.resources

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import ru.zveron.design.shimmering.shimmeringBackground

@Immutable
sealed interface ZveronImage {
    @Immutable
    class ResourceImage(@DrawableRes val resId: Int): ZveronImage

    @Immutable
    class RemoteImage(val imageUrl: String): ZveronImage
}

val LocalLoadingImageSize = staticCompositionLocalOf { 50.dp }

@Composable
fun ZveronImage(
    zveronImage: ZveronImage,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    contentDescription: String? = null,
    loadingImageModifier: Modifier = Modifier
        .size(LocalLoadingImageSize.current)
        .clip(CircleShape)
        .background(Color.LightGray),
    readyImageModifier: Modifier = Modifier,
) {
    when (zveronImage) {
        is ZveronImage.RemoteImage -> {
            SubcomposeAsyncImage(
                model = zveronImage.imageUrl,
                modifier = modifier,
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter,
                contentDescription = contentDescription,
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    BoxWithConstraints(
                        modifier = loadingImageModifier,
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .shimmeringBackground(this.maxWidth)
                        )
                    }
                } else {
                    SubcomposeAsyncImageContent(modifier = readyImageModifier)
                }
            }
        }
        is ZveronImage.ResourceImage -> {
            Image(
                painter = painterResource(zveronImage.resId),
                contentDescription = contentDescription,
                modifier = modifier.then(readyImageModifier),
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter,
            )
        }
    }
}
