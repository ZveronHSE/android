package ru.zveron.personal_profile.edit_profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.components.ActionButton
import ru.zveron.design.components.PhotoUploadState
import ru.zveron.design.fonts.Rubik
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.shimmering.shimmeringBackground
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.personal_profile.R
import ru.zveron.design.R as DesignR

@Composable
internal fun EditProfile(
    name: String,
    setName: (String) -> Unit,
    surname: String,
    setSurname: (String) -> Unit,
    editProfileUiState: EditProfileUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onGalleryPhotoClick: () -> Unit = {},
    onCameraPhotoClick: () -> Unit = {},
    onReadyClick: () -> Unit = {},
    onPhotoRetryClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painterResource(DesignR.drawable.ic_close),
                    null,
                )
            }

            Text(
                text = stringResource(R.string.edit_profile_title),
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = (27.6).sp,
                    letterSpacing = (-0.69).sp,
                ),
                modifier = Modifier.align(Alignment.Center),
            )
        }

        Spacer(Modifier.height(40.dp))

        UploadPhoto(
            photoUploadState = editProfileUiState.photoUiState,
            onGalleryPhotoClick = onGalleryPhotoClick,
            onCameraPhotoClick = onCameraPhotoClick,
            onRetryClick = onPhotoRetryClick,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(Modifier.height(32.dp))

        TextField(
            value = name,
            onValueChange = setName,
            placeholder = { Text(stringResource(R.string.edit_profile_name_placeholder)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 34.dp),
        )

        Spacer(Modifier.height(6.dp))

        TextField(
            value = surname,
            onValueChange = setSurname,
            placeholder = { Text(stringResource(R.string.edit_profile_surname_placeholder)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 34.dp),
        )

        Spacer(Modifier.weight(1f))

        ActionButton(
            onClick = onReadyClick,
            enabled = editProfileUiState.photoUiState is PhotoUploadState.Success && !editProfileUiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            Text(
                stringResource(R.string.edit_profile_ready),
                style = MaterialTheme.typography.body1,
            )

            if (editProfileUiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize().shimmeringBackground(maxWidth))
            }
        }
    }
}

@Composable
private fun UploadPhoto(
    photoUploadState: PhotoUploadState,
    modifier: Modifier = Modifier,
    onGalleryPhotoClick: () -> Unit = {},
    onCameraPhotoClick: () -> Unit = {},
    onRetryClick: () -> Unit = {},
) {
    when (photoUploadState) {
        is PhotoUploadState.Error -> UploadAvatarError(
            image = photoUploadState.image,
            onRetryClick = onRetryClick,
            modifier = modifier,
        )

        is PhotoUploadState.Loading -> UploadAvatarLoading(
            image = photoUploadState.image,
            modifier = modifier,
        )

        is PhotoUploadState.Success -> UploadAvatarSuccess(
            image = photoUploadState.image,
            onGalleryPhotoClick = onGalleryPhotoClick,
            onCameraPhotoClick = onCameraPhotoClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun UploadAvatarSuccess(
    image: ZveronImage,
    modifier: Modifier = Modifier,
    onGalleryPhotoClick: () -> Unit = {},
    onCameraPhotoClick: () -> Unit = {},
) {
    val expanded = remember { mutableStateOf(false) }

    Box(
        modifier = modifier.wrapContentSize(Alignment.TopStart),
    ) {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded.value = false
                    onCameraPhotoClick.invoke()
                },
            ) {
                Text(
                    text = stringResource(R.string.edit_profile_camera_item),
                    style = TextStyle(
                        fontFamily = Rubik,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 18.96.sp,
                    ),
                )
            }

            DropdownMenuItem(
                onClick = {
                    expanded.value = false
                    onGalleryPhotoClick.invoke()
                },
            ) {
                Text(
                    text = stringResource(R.string.edit_profile_gallery_item),
                    style = TextStyle(
                        fontFamily = Rubik,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 18.96.sp,
                    ),
                )
            }
        }

        ZveronImage(
            zveronImage = image,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .clickable { expanded.value = !expanded.value },
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun UploadAvatarLoading(
    image: ZveronImage,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .size(140.dp)
            .clip(CircleShape)
    ) {
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

@Composable
private fun UploadAvatarError(
    image: ZveronImage,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit = {},
    retryHint: String = stringResource(R.string.edit_profile_photo_retry),
) {
    Box(
        modifier
            .size(140.dp)
            .clip(CircleShape)
            .clickable(onClick = onRetryClick, onClickLabel = retryHint)
    ) {
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
                    painterResource(DesignR.drawable.ic_refresh),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )

                Text(
                    text = retryHint,
                    style = TextStyle(
                        fontFamily = Rubik,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        lineHeight = (14.22).sp,
                        color = Color.White,
                    )
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EditProfilePreview() {
    ZveronTheme {
        val name = remember { mutableStateOf("") }

        val surname = remember { mutableStateOf("") }

        val editProfileUiState = EditProfileUiState(
            photoUiState = PhotoUploadState.Success(ZveronImage.ResourceImage(DesignR.drawable.ic_no_avatar)),
            isLoading = false,
        )

        EditProfile(
            name = name.value,
            setName = { name.value = it },
            surname = surname.value,
            setSurname = { surname.value = it },
            editProfileUiState = editProfileUiState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun EditProfileLoadingPreview() {
    ZveronTheme {
        val name = remember { mutableStateOf("") }

        val surname = remember { mutableStateOf("") }

        val editProfileUiState = EditProfileUiState(
            photoUiState = PhotoUploadState.Loading(ZveronImage.ResourceImage(DesignR.drawable.ic_no_avatar)),
            isLoading = true,
        )

        EditProfile(
            name = name.value,
            setName = { name.value = it },
            surname = surname.value,
            setSurname = { surname.value = it },
            editProfileUiState = editProfileUiState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun EditProfileErrorPreview() {
    ZveronTheme {
        val name = remember { mutableStateOf("") }

        val surname = remember { mutableStateOf("") }

        val editProfileUiState = EditProfileUiState(
            photoUiState = PhotoUploadState.Error(ZveronImage.ResourceImage(DesignR.drawable.ic_no_avatar)),
            isLoading = false,
        )

        EditProfile(
            name = name.value,
            setName = { name.value = it },
            surname = surname.value,
            setSurname = { surname.value = it },
            editProfileUiState = editProfileUiState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}