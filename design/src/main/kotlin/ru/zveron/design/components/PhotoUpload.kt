package ru.zveron.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.R
import ru.zveron.design.fonts.Rubik
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.gray5

@Immutable
sealed class PhotoUploadState(open val image: ZveronImage) {
    data class Success(override val image: ZveronImage) : PhotoUploadState(image)

    data class Loading(override val image: ZveronImage) : PhotoUploadState(image)

    data class Error(override val image: ZveronImage) : PhotoUploadState(image)
}

@Composable
fun AddPhoto(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    label: String = stringResource(R.string.photo_upload_hint),
) {
    Card(
        modifier = modifier.size(136.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            modifier = Modifier
                .clickable(onClick = onClick, onClickLabel = label)
                .semantics(mergeDescendants = true) {
                    contentDescription = label
                },
        ) {
            Icon(
                painterResource(R.drawable.ic_add_gradient),
                contentDescription = null,
                tint = Color.Unspecified,
            )

            Text(
                text = label,
                style = TextStyle(
                    fontFamily = Rubik,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = gray5,
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun PhotoUpload(
    photoUploadState: PhotoUploadState,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit = {},
) {
    when (photoUploadState) {
        is PhotoUploadState.Error -> PhotoUploadError(
            image = photoUploadState.image,
            modifier = modifier,
            onRetryClick = onRetryClick,
        )

        is PhotoUploadState.Loading -> PhotoUploadLoading(
            image = photoUploadState.image,
            modifier = modifier,
        )

        is PhotoUploadState.Success -> PhotoUploadSucces(
            image = photoUploadState.image,
            modifier = modifier,
        )
    }
}


@Composable
private fun PhotoUploadSucces(
    image: ZveronImage,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.size(136.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        ZveronImage(
            zveronImage = image,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun PhotoUploadLoading(
    image: ZveronImage,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.size(136.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box {
            ZveronImage(
                zveronImage = image,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .background(Color(0x80000000))
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun PhotoUploadError(
    image: ZveronImage,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit,
    retryHint: String = stringResource(R.string.photo_retry_hint),
) {

    Card(
        modifier = modifier.size(136.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(modifier = Modifier.clickable(onClick = onRetryClick, onClickLabel = retryHint)) {
            ZveronImage(
                zveronImage = image,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .background(Color(0x80000000))
                    .fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .semantics(mergeDescendants = true) {
                            contentDescription = retryHint
                        },
                ) {
                    Icon(
                        painterResource(R.drawable.ic_refresh),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )

                    Text(
                        text = retryHint,
                        style = TextStyle(
                            fontFamily = Rubik,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            lineHeight = (14.22).sp,
                            color = Color.White,
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PhotoUploadPreview() {
    val states = listOf(
        PhotoUploadState.Error(ZveronImage.ResourceImage(R.drawable.cool_dog)),
        PhotoUploadState.Loading(ZveronImage.ResourceImage(R.drawable.cool_dog)),
        PhotoUploadState.Success(ZveronImage.ResourceImage(R.drawable.cool_dog)),
    )

    ZveronTheme {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.background)
        ) {
            item {
                AddPhoto()
            }

            items(states) {
                PhotoUpload(photoUploadState = it)
            }
        }
    }
}