package ru.zveron.lots_feed.filters_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.components.ActionButton
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.lots_feed.R
import ru.zveron.lots_feed.filters_screen.ui.categories.RootCategoriesUiState
import ru.zveron.lots_feed.filters_screen.ui.categories.RootCategoryUiState
import ru.zveron.design.R as DesignR
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.filters_screen.ui.categories.ChildCategory
import ru.zveron.lots_feed.filters_screen.ui.categories.ChildrenCategoriesUiState
import ru.zveron.lots_feed.filters_screen.ui.lot_form.LotFormUiState
import ru.zveron.lots_feed.filters_screen.ui.parameters.FiltersParametersUiState
import ru.zveron.lots_feed.filters_screen.ui.parameters.ParameterUiState
import ru.zveron.lots_feed.filters_screen.ui.parameters.Parameters

@Composable
fun FilterScreen(
    rootCategoriesUiState: RootCategoriesUiState,
    childCategoriesUiState: ChildrenCategoriesUiState,
    parametersState: FiltersParametersUiState,
    lotFormUiState: LotFormUiState,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onDoneClicked: () -> Unit = {},
    onRootCategorySelected: (Int) -> Unit = {},
    onChildCategoryClicked: () -> Unit = {},
    onLotFormClicked: () -> Unit = {},
    onParameterClicked: (Int) -> Unit = {},
) {
    Column(
        modifier = modifier.background(MaterialTheme.colors.background),
    ) {
        FiltersAppBar(
            onBackClicked = onBackClicked,
            modifier = Modifier.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        )

        Spacer(Modifier.height(8.dp))

        RootCategoriesSelector(
            rootCategoriesUiState = rootCategoriesUiState,
            onRootCategorySelected = onRootCategorySelected,
        )

        DefaultSectionSpacer()

        when (lotFormUiState) {
            LotFormUiState.Hidden -> {
            }

            is LotFormUiState.Show -> {
                LotForm(
                    lotFormUiState = lotFormUiState,
                    onLotFormClicked = onLotFormClicked,
                )
                DefaultSectionSpacer()
            }
        }

        when (childCategoriesUiState) {
            ChildrenCategoriesUiState.Hidden -> {
            }

            is ChildrenCategoriesUiState.Show -> {
                ChildCategory(
                    childCategoryUiState = childCategoriesUiState,
                    onChildCategoryClicked = onChildCategoryClicked,
                )

                if (parametersState !is FiltersParametersUiState.Hidden) {
                    Divider(Modifier.padding(horizontal = 17.dp))
                }
            }
        }

        Parameters(
            state = parametersState,
            hasTopCorners = childCategoriesUiState is ChildrenCategoriesUiState.Hidden,
            onParameterClicked = onParameterClicked,
        )

        Spacer(Modifier.weight(1f))

        ActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            onClick = onDoneClicked,
        ) {
            Text(
                text = stringResource(R.string.filters_button_title),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                )
            )
        }
    }
}

@Composable
private fun DefaultSectionSpacer() {
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun FiltersAppBar(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .height(48.dp),
    ) {
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                painter = painterResource(DesignR.drawable.ic_close),
                contentDescription = stringResource(R.string.back_hint),
            )
        }

        Text(
            text = stringResource(R.string.filters_app_bar_title),
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )
        )
    }
}

@Preview
@Composable
private fun FilterScreenLoadingPreview() {
    ZveronTheme {
        FilterScreen(
            rootCategoriesUiState = RootCategoriesUiState.Loading,
            childCategoriesUiState = ChildrenCategoriesUiState.Hidden,
            parametersState = FiltersParametersUiState.Loading,
            lotFormUiState = LotFormUiState.Hidden,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun FilterScreenRootCategorySelectedPreview() {
    val rootCategoriesUiState = RootCategoriesUiState.Success(
        categories = listOf(
            RootCategoryUiState(1, "Животные"),
            RootCategoryUiState(2, "Товары для животных")
        ),
        selectedCategoryId = 1,
    )

    val childCategoriesUiState = ChildrenCategoriesUiState.Show(ZveronText.RawString("Животные"))

    val filtersState = FiltersParametersUiState.Success(
        listOf(
            ParameterUiState(1, "Порода", isUnderlined = false),
            ParameterUiState(2, "Цвет", isUnderlined = true),
            ParameterUiState(3, "Возраст", isUnderlined = false),
        )
    )

    val lotFormUiState = LotFormUiState.Show(ZveronText.RawResource(R.string.lot_form_title))

    ZveronTheme {
        FilterScreen(
            rootCategoriesUiState = rootCategoriesUiState,
            childCategoriesUiState = childCategoriesUiState,
            parametersState = filtersState,
            lotFormUiState = lotFormUiState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun FilterScreenRootCategoryUnselectedPreview() {
    val rootCategoriesUiState = RootCategoriesUiState.Success(
        categories = listOf(
            RootCategoryUiState(1, "Животные"),
            RootCategoryUiState(2, "Товары для животных")
        ),
        selectedCategoryId = null,
    )

    val childCategoriesUiState = ChildrenCategoriesUiState.Hidden

    val filtersState = FiltersParametersUiState.Hidden

    val lotFormUiState = LotFormUiState.Show(ZveronText.RawResource(R.string.lot_form_title))

    ZveronTheme {
        FilterScreen(
            rootCategoriesUiState = rootCategoriesUiState,
            childCategoriesUiState = childCategoriesUiState,
            parametersState = filtersState,
            lotFormUiState = lotFormUiState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun FilterScreenRootCategoryNoParametersPreview() {
    val rootCategoriesUiState = RootCategoriesUiState.Success(
        categories = listOf(
            RootCategoryUiState(1, "Животные"),
            RootCategoryUiState(2, "Товары для животных")
        ),
        selectedCategoryId = 1,
    )

    val childCategoriesUiState =
        ChildrenCategoriesUiState.Show(ZveronText.RawResource(R.string.child_category_selector_title))

    val filtersState = FiltersParametersUiState.Hidden

    val lotFormUiState = LotFormUiState.Show(ZveronText.RawResource(R.string.lot_form_title))

    ZveronTheme {
        FilterScreen(
            rootCategoriesUiState = rootCategoriesUiState,
            childCategoriesUiState = childCategoriesUiState,
            parametersState = filtersState,
            lotFormUiState = lotFormUiState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}