package ru.zveron.lots_feed.filters_screen.ui.parameters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.R
import ru.zveron.design.shimmering.shimmeringBackground

@Composable
internal fun Parameters(
    state: FiltersParametersUiState,
    hasTopCorners: Boolean,
    onParameterClicked: (Int) -> Unit,
) {
    when (state) {
        FiltersParametersUiState.Loading -> ParametersLoading(hasTopCorners = hasTopCorners)
        is FiltersParametersUiState.Success -> SuccessParameters(
            parameters = state.parameters,
            hasTopCorners = hasTopCorners,
            onClick = onParameterClicked,
        )

        FiltersParametersUiState.Hidden -> {}
    }
}

@Composable
private fun SuccessParameters(
    parameters: List<ParameterUiState>,
    hasTopCorners: Boolean,
    onClick: (Int) -> Unit = {},
) {
    parameters.forEachIndexed { index, parameter ->
        val modifier = when {
            index == 0 && hasTopCorners -> Modifier.clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            )

            index == parameters.lastIndex -> Modifier.clip(
                RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )

            else -> Modifier
        }
        key(parameter.id) {
            ParameterRow(
                parameter = parameter,
                modifier = modifier,
                hasDivider = index != parameters.lastIndex,
                onClick = onClick,
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
        val textDecoration =
            if (parameter.isUnderlined) TextDecoration.Underline else TextDecoration.None

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
            painter = painterResource(R.drawable.ic_next),
            contentDescription = null,
        )
    }

    if (hasDivider) {
        Divider(Modifier.padding(horizontal = 17.dp))
    }
}

@Composable
private fun ParametersLoading(
    hasTopCorners: Boolean,
    modifier: Modifier = Modifier,
) {
    val clipModifier = if (hasTopCorners) {
        Modifier.clip(RoundedCornerShape(16.dp))
    } else {
        Modifier.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
    }

    BoxWithConstraints(
        modifier = modifier
            .then(clipModifier)
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