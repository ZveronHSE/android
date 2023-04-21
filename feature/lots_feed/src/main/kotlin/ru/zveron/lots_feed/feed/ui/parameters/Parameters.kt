package ru.zveron.lots_feed.feed.ui.parameters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zveron.design.R
import ru.zveron.design.components.Chip
import ru.zveron.design.components.LoadingChip
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.gray1
import ru.zveron.design.theme.gray5

@Composable
fun ParametersRow(
    uiState: ParametersUiState,
    modifier: Modifier = Modifier,
    onParameterClick: (Int) -> Unit = {},
) {
    if (uiState is ParametersUiState.Hidden) {
        return
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        if (uiState is ParametersUiState.Loading) {
            items(3) {
                LoadingChip()
            }
        } else if(uiState is ParametersUiState.Success) {
            items(uiState.parameters, key = { it.id }) { parameter ->
                Chip(
                    title = ZveronText.RawString(parameter.title),
                    isActive = parameter.isActive,
                    onClick = { onParameterClick.invoke(parameter.id) },
                    textMaxWidth = 180.dp,
                    trailSlot = {
                        val textColor = if (parameter.isActive) gray1 else gray5

                        Icon(
                            painter = painterResource(R.drawable.ic_down_triangle),
                            contentDescription = null,
                            tint = textColor,
                        )
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ParametersLoadingPreview() {
    val parameters = ParametersUiState.Loading

    ParametersRow(uiState = parameters, modifier = Modifier.fillMaxWidth())
}

@Preview(showBackground = true)
@Composable
private fun ParametersSuccessPreview() {
    val parameters = ParametersUiState.Success(
        listOf(
            ParameterUiState(
                id = 1,
                title = "Лабрадор, маламут, немецкая овчарка",
                isActive = true
            ),
            ParameterUiState(
                id = 2,
                title = "Размер",
                isActive = false,
            ),
        )
    )

    ParametersRow(uiState = parameters, modifier = Modifier.fillMaxWidth())
}