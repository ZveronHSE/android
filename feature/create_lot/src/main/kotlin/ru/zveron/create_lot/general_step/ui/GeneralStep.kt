package ru.zveron.create_lot.general_step.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.create_lot.R
import ru.zveron.design.components.ActionButton
import ru.zveron.design.components.AddPhoto
import ru.zveron.design.components.Chip
import ru.zveron.design.components.LoadingChip
import ru.zveron.design.components.PhotoUpload
import ru.zveron.design.components.PhotoUploadState
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.gray4
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.design.R as DesignR

@Composable
internal fun GeneralStepLotCreate(
    rootCategoriesUiState: RootCategoriesUiState,
    photoUploadUiState: PhotoUploadUiState,
    nameInput: String,
    setName: (String) -> Unit,
    continueButtonState: ContinueButtonState,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onRootCategoryClick: (Int) -> Unit = {},
    onAddPhotoClick: () -> Unit = {},
    onPhotoRetryClick: (String) -> Unit = {},
    onContinueClick: () -> Unit = {},
) {
    Column(modifier = modifier.padding(top = 16.dp)) {
        GeneralStepHeader(onCloseClick = onCloseClick)

        Spacer(Modifier.height(32.dp))

        RootCategories(
            rootCategoriesUiState = rootCategoriesUiState,
            onRootCategoryClick = onRootCategoryClick,
        )

        Spacer(Modifier.height(32.dp))

        RootCategoriesPhotos(
            photoUploadUiState = photoUploadUiState,
            onAddPhotoClick = onAddPhotoClick,
            onRetryClicked = onPhotoRetryClick,
        )
        
        Spacer(Modifier.height(32.dp))

        GeneralStepNameInput(nameInput = nameInput, setName = setName)

        Spacer(Modifier.weight(1f))

        ActionButton(
            enabled = continueButtonState.isEnabled,
            onClick = onContinueClick,
            modifier = Modifier
                .padding(bottom = 26.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.continue_button_title),
                style = MaterialTheme.typography.body1,
            )
        }
    }
}

@Composable
private fun GeneralStepHeader(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxWidth()) {
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                painterResource(DesignR.drawable.ic_close),
                null,
            )
        }

        Text(
            stringResource(R.string.first_step_header),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = (27.6).sp,
                letterSpacing = (-0.69).sp,
            ),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun RootCategories(
    rootCategoriesUiState: RootCategoriesUiState,
    modifier: Modifier = Modifier,
    onRootCategoryClick: (Int) -> Unit = {},
) {
    Text(
        text = stringResource(R.string.root_categories_header),
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 34.sp,
            letterSpacing = (0.36).sp,
        ),
        modifier = Modifier.padding(start = 16.dp),
    )

    Spacer(Modifier.height(16.dp))

    when (rootCategoriesUiState) {
        RootCategoriesUiState.Loading -> RootCategoriesLoading(modifier)
        is RootCategoriesUiState.Success -> RootCategoriesSuccess(
            rootCategories = rootCategoriesUiState.rootCategories,
            selectedRootCategoryId = rootCategoriesUiState.selectedRootCategoryId,
            modifier = modifier,
            onRootCategoryClick = onRootCategoryClick,
        )
    }
}

@Composable
private fun RootCategoriesLoading(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(start = 16.dp),
    ) {
        repeat(2) {
            LoadingChip()
        }
    }
}

@Composable
private fun RootCategoriesSuccess(
    rootCategories: ListWrapper<RootCategoryUiState>,
    selectedRootCategoryId: Int?,
    modifier: Modifier = Modifier,
    onRootCategoryClick: (Int) -> Unit = {},
) {
    LazyRow(
        modifier = modifier.padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(rootCategories.list, { it.id }) { rootCategory ->
            val clicker = remember {
                { onRootCategoryClick.invoke(rootCategory.id) }
            }

            Chip(
                title = ZveronText.RawString(rootCategory.name),
                isActive = rootCategory.id == selectedRootCategoryId,
                onClick = clicker,
            )
        }
    }
}

@Composable
private fun RootCategoriesPhotos(
    photoUploadUiState: PhotoUploadUiState,
    onAddPhotoClick: () -> Unit = {},
    onRetryClicked: (String) -> Unit = {}
) {
    Text(
        text = stringResource(R.string.photos_header),
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 34.sp,
            letterSpacing = (0.36).sp,
        ),
        modifier = Modifier.padding(start = 16.dp),
    )

    Spacer(Modifier.height(16.dp))

    LazyRow(
        modifier = Modifier.padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            AddPhoto(onClick = onAddPhotoClick)
        }

        items(photoUploadUiState.states.list, { it.first }) {
            val clicker = remember {
                { onRetryClicked.invoke(it.first) }
            }

            PhotoUpload(photoUploadState = it.second, onRetryClick = clicker)
        }
    }
}

@Composable
private fun GeneralStepNameInput(
    nameInput: String,
    setName: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.name_header),
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 34.sp,
            letterSpacing = (0.36).sp,
        ),
        modifier = Modifier.padding(start = 16.dp),
    )

    Spacer(Modifier.height(16.dp))

    TextField(
        value = nameInput,
        onValueChange = setName,
        placeholder = { Text(stringResource(R.string.lot_name_placeholder)) },
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

    Text(
        text = stringResource(R.string.name_example),
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            color = gray4,
        ),
        modifier = Modifier.padding(start = 16.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun GeneralStepPreview() {
    ZveronTheme {
        val rootCategoriesUiState = RootCategoriesUiState.Success(
            rootCategories = ListWrapper(
                listOf(
                    RootCategoryUiState(1, "Животные"),
                    RootCategoryUiState(2, "Товары для животных"),
                )
            ),
            selectedRootCategoryId = 1,
        )

        val photoUploadUiState = PhotoUploadUiState(
            states = ListWrapper(
                listOf(
                    "1" to PhotoUploadState.Error(ZveronImage.ResourceImage(DesignR.drawable.cool_dog)),
                    "2" to PhotoUploadState.Loading(ZveronImage.ResourceImage(DesignR.drawable.cool_dog)),
                    "3" to PhotoUploadState.Success(ZveronImage.ResourceImage(DesignR.drawable.cool_dog)),
                )
            ),
        )

        val continueButtonState = ContinueButtonState(true)

        val input = remember { mutableStateOf("") }

        GeneralStepLotCreate(
            rootCategoriesUiState = rootCategoriesUiState,
            photoUploadUiState = photoUploadUiState,
            continueButtonState = continueButtonState,
            nameInput= input.value,
            setName = { input.value = it },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GeneralStepLoadingPreview() {
    ZveronTheme {
        val rootCategoriesUiState = RootCategoriesUiState.Loading

        val photoUploadUiState = PhotoUploadUiState(
            states = ListWrapper(
                listOf(
                    "1" to PhotoUploadState.Error(ZveronImage.ResourceImage(DesignR.drawable.cool_dog)),
                    "2" to PhotoUploadState.Loading(ZveronImage.ResourceImage(DesignR.drawable.cool_dog)),
                    "3" to PhotoUploadState.Success(ZveronImage.ResourceImage(DesignR.drawable.cool_dog)),
                )
            ),
        )

        val continueButtonState = ContinueButtonState(false)

        val input = remember { mutableStateOf("") }

        GeneralStepLotCreate(
            rootCategoriesUiState = rootCategoriesUiState,
            photoUploadUiState = photoUploadUiState,
            continueButtonState = continueButtonState,
            nameInput= input.value,
            setName = { input.value = it },
            modifier = Modifier.fillMaxSize(),
        )
    }
}