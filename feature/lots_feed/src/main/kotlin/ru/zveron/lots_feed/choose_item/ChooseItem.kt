package ru.zveron.lots_feed.choose_item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.enabledButtonGradient
import ru.zveron.design.theme.gray1
import ru.zveron.design.theme.gray3
import ru.zveron.lots_feed.R
import ru.zveron.design.R as DesignR

@Composable
fun ChooseItemScreen(
    items: List<Pair<Int, String>>,
    title: String,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit = {},
    onBackClicked: () -> Unit = {},
) {
    val searchInput = remember { mutableStateOf("") }

    val visibleItems by remember(items) {
        derivedStateOf {
            items.filter {
                it.second.toLowerCase(Locale.current).contains(searchInput.value.toLowerCase(Locale.current))
            }
        }
    }

    Column(
        modifier = modifier.background(MaterialTheme.colors.background),
    ) {
        IconButton(
            onClick = onBackClicked,
        ) {
            Icon(
                painter = painterResource(DesignR.drawable.ic_close),
                contentDescription = stringResource(R.string.back_hint),
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = title,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(Modifier.height(24.dp))

        val interactionSource = remember { MutableInteractionSource() }

        BasicTextField(
            value = searchInput.value,
            onValueChange = { searchInput.value = it },
            interactionSource = interactionSource,
            textStyle = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = gray3,
            ),
            cursorBrush = enabledButtonGradient,
        ) { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(gray1),
            ) {
                val isFocused = interactionSource.collectIsFocusedAsState()

                Spacer(Modifier.width(8.dp))

                Icon(
                    painterResource(DesignR.drawable.ic_search),
                    null,
                    tint = gray3,
                )

                Spacer(Modifier.width(8.dp))

                if (searchInput.value.isEmpty() && !isFocused.value) {
                    Text(
                        text = stringResource(R.string.search_input_hint),
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = gray3,
                        )
                    )
                }

                innerTextField.invoke()

                Spacer(modifier = Modifier.weight(1f))

                if (!searchInput.value.isEmpty()) {
                    IconButton(onClick = { searchInput.value = "" }) {
                        Icon(
                            painter = painterResource(DesignR.drawable.ic_close_gradient),
                            contentDescription = stringResource(R.string.search_clear_hint),
                            tint = Color.Unspecified,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            items(visibleItems, key = { it.first }) {
                val clicker = remember {
                    { onItemClick.invoke(it.first) }
                }
                Text(
                    text = it.second,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clickable(onClick = clicker, onClickLabel = it.second),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChooseItemPreview() {
    ZveronTheme {
        ChooseItemScreen(
            items = listOf(
                1 to "Собаки",
                2 to "Кошки",
                3 to "Грызуны"
            ),
            title = "Виды животных",
            modifier = Modifier.fillMaxSize(),
        )
    }
}