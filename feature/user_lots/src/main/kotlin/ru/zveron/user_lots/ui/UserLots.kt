package ru.zveron.user_lots.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.components.ActionButton
import ru.zveron.design.lots.LotRow
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.enabledButtonGradient
import ru.zveron.design.theme.gray2
import ru.zveron.design.theme.gray3
import ru.zveron.design.theme.gray5
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.user_lots.R
import ru.zveron.design.R as DesignR

@Composable
internal fun UserLots(
    state: UserLotsUiState,
    modifier: Modifier = Modifier,
    onTabClick: (UserLotTab) -> Unit = {},
    onAddLotClick: () -> Unit = {},
    onLotClick: (Long) -> Unit = {},
    onRetryClicked: () -> Unit = {},
) {
    when (state) {
        UserLotsUiState.Loading -> LoadingUserLots(modifier)
        is UserLotsUiState.Success -> SuccessUserLots(
            userLotsUiState = state,
            onTabClick = onTabClick,
            onLotClick = onLotClick,
            onAddLotClick = onAddLotClick,
        )
        UserLotsUiState.Error -> ErrorUserLots(modifier, onRetryClicked)
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ErrorUserLots(
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.user_lots_title),
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
            ),
            color = gray5,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(Modifier.weight(1f))

        Icon(
            painterResource(DesignR.drawable.ic_warning),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            tint = Color.Unspecified,
        )

        Text(
            text = stringResource(R.string.user_lots_error),
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = gray3,
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.user_lots_error_button_title),
            style = TextStyle(
                brush = enabledButtonGradient,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(
                    role = Role.Button,
                    onClickLabel = stringResource(R.string.user_lots_error_button_title),
                    onClick = onRetryClicked,
                ),
        )

        Spacer(Modifier.weight(1f))
    }
}



@Composable
private fun LoadingUserLots(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.user_lots_title),
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
            ),
            color = gray5,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(Modifier.weight(1f))

        CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun SuccessUserLots(
    userLotsUiState: UserLotsUiState.Success,
    modifier: Modifier = Modifier,
    onTabClick: (UserLotTab) -> Unit = {},
    onAddLotClick: () -> Unit = {},
    onLotClick: (Long) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        item {
            Spacer(Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.user_lots_title),
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                ),
                color = gray5,
            )

            Spacer(Modifier.height(32.dp))
        }

        item {
            UserLotsTabs(userLotsUiState.currentTab, onTabClick = onTabClick)

            Spacer(Modifier.height(24.dp))
        }

        UserLotsList(
            userLotsUiState.currentLots,
            onLotClick = onLotClick,
            onAddLotClick = onAddLotClick,
        )
    }
}

@Composable
private fun UserLotsTabs(
    currentTab: UserLotTab,
    modifier: Modifier = Modifier,
    onTabClick: (UserLotTab) -> Unit = {},
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier) {
        UserLotTab.values().forEach { tab ->
            key(tab) {
                val textStyle = if (tab == currentTab) {
                    TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = gray5,
                    )
                } else {
                    TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = gray2,
                    )
                }

                val clicker = remember {
                    { onTabClick.invoke(tab) }
                }

                Text(
                    text = stringResource(tab.title),
                    style = textStyle,
                    modifier = Modifier.clickable(
                        role = Role.Button,
                        onClickLabel = stringResource(tab.title),
                        onClick = clicker
                    ),
                )
            }
        }
    }
}

private fun LazyListScope.UserLotsList(
    lots: ListWrapper<LotUiState>,
    onLotClick: (Long) -> Unit = {},
    onAddLotClick: () -> Unit = {},
) {
    itemsIndexed(items = lots.list, key = { _: Int, lot: LotUiState -> lot.id }) { index, lot ->
        val clicker = remember {
            { onLotClick.invoke(lot.id) }
        }

        LotRow(
            zveronImage = lot.image,
            title = lot.title,
            price = lot.price,
            views = lot.views,
            likes = lot.likes,
            isActive = lot.isActive,
            onCardClick = clicker,
        )

        val spacing = if (index != lots.list.lastIndex) 16.dp else 24.dp

        Spacer(Modifier.height(spacing))
    }

    item {
        ActionButton(
            onClick = onAddLotClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.add_lot_button_title),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Preview
@Composable
private fun UserLotsSuccessPreview() {
    ZveronTheme {
        val state = UserLotsUiState.Success(
            currentTab = UserLotTab.ACTIVE,
            currentLots = ListWrapper(listOf(
                LotUiState(
                    id = 1,
                    title = "Собака Корги. 3 года. ОТдам на передержку",
                    price = "40 000 ₽",
                    image = ZveronImage.ResourceImage(DesignR.drawable.cool_dog),
                    isActive = true,
                    views = 100,
                    likes = 10_000,
                ),
                LotUiState(
                    id = 2,
                    title = "Собака Самоед (помесь). 3 года",
                    price = "40 000 ₽",
                    image = ZveronImage.ResourceImage(DesignR.drawable.cool_dog),
                    isActive = true,
                    views = 100,
                    likes = 10_000,
                )
            )),
        )

        SuccessUserLots(
            userLotsUiState = state,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        )
    }
}

@Preview
@Composable
private fun UserLotsLoadingPreview() {
    ZveronTheme {
        LoadingUserLots(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        )
    }
}

@Preview
@Composable
private fun UserLotsErrorPreview() {
    ZveronTheme {
        ErrorUserLots(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        )
    }
}
