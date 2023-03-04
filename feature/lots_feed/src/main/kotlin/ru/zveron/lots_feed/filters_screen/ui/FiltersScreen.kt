package ru.zveron.lots_feed.filters_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.components.ActionButton
import ru.zveron.design.shimmering.shimmeringBackground
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.lots_feed.R
import ru.zveron.design.R as DesignR

@Composable
fun FilterScreen(
    parametersState: FiltersParametersUiState,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onDoneClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier.background(MaterialTheme.colors.background),
    ) {
        FiltersAppBar(
            onBackClicked = onBackClicked,
            modifier = Modifier.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        )

        DefaultSectionSpacer()

        Parameters(
            state = parametersState,
        )
        
        Spacer(Modifier.weight(1f))

        ActionButton(
            modifier = Modifier.fillMaxWidth(),
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

@Composable
private fun Parameters(
    state: FiltersParametersUiState,
) {
    when (state) {
        FiltersParametersUiState.Loading -> ParametersLoading()
        is FiltersParametersUiState.Success -> SuccessParameters(parameters = state.parameters)
    }
}

@Composable
private fun SuccessParameters(
    parameters: List<ParameterUiState>,
) {
    parameters.forEachIndexed { index, parameter ->
        val modifier = when (index) {
            0 -> Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            parameters.lastIndex -> Modifier.clip(
                RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
            else -> Modifier
        }
        key(parameter.id) {
            ParameterRow(
                parameter = parameter,
                modifier = modifier,
                hasDivider = index != parameters.lastIndex,
            )
        }
    }
}

@Composable
private fun ParameterRow(
    parameter: ParameterUiState,
    hasDivider: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {},
) {
    val clicker = remember {
        { onClick.invoke(parameter.id) }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .height(48.dp)
            .clickable(onClickLabel = parameter.title, onClick = clicker)
            .padding(horizontal = 17.dp)
    ) {
        val textDecoration = if (parameter.isUnderlined) TextDecoration.Underline else TextDecoration.None

        Text(
            text = parameter.title,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            ),
            textDecoration = textDecoration,
        )

        Spacer(Modifier.weight(1f))

        Icon(
            painter = painterResource(DesignR.drawable.ic_next),
            contentDescription = null,
        )
    }

    if (hasDivider) {
        Divider(Modifier.padding(horizontal = 17.dp))
    }
}

@Composable
private fun ParametersLoading(
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .height(192.dp)
            .background(MaterialTheme.colors.surface)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .shimmeringBackground(
                    width = this.maxWidth,
                    shimmerColor = MaterialTheme.colors.background,
                )
        )
    }
}

@Preview
@Composable
private fun FilterScreenLoadingPreview() {
    ZveronTheme {
        FilterScreen(
            parametersState = FiltersParametersUiState.Loading,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun FilterScreenPreview() {
    val state = FiltersParametersUiState.Success(
        listOf(
            ParameterUiState(1, "Порода", isUnderlined = false),
            ParameterUiState(2, "Цвет", isUnderlined = true),
            ParameterUiState(3, "Возраст", isUnderlined = false),
        )
    )

    ZveronTheme {
        FilterScreen(
            parametersState = state,
            modifier = Modifier.fillMaxSize(),
        )
    }
}