package ru.zveron.lots_feed.feed.ui.parameters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zveron.design.components.Chip
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.shimmering.shimmeringBackground

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
                BoxWithConstraints(
                    modifier = Modifier
                        .width(120.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray),
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .shimmeringBackground(this.maxWidth)
                    )
                }
            }
        } else if(uiState is ParametersUiState.Success) {
            items(uiState.parameters, key = { it.id }) { parameter ->
                Chip(
                    title = ZveronText.RawString(parameter.title),
                    isActive = parameter.isActive,
                    onClick = { onParameterClick.invoke(parameter.id) },
                    textMaxWidth = 180.dp,
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