package ru.zveron.lots_feed.feed.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.fonts.Rubik
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.lots_feed.R

@Composable
fun SortDropdown(
    modifier: Modifier = Modifier,
    onSortTypeSelected: (SortType) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    val items = SortType.values()
    var selectedIndex by remember { mutableStateOf(0) }
    Box(
        modifier = modifier.wrapContentSize(Alignment.TopStart)
    ) {
        Row(
            modifier = Modifier.clickable(onClick = { expanded = true }),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(items[selectedIndex].titleRes),
                style = TextStyle(
                    fontFamily = Rubik,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 18.96.sp,
                )
            )

            Spacer(Modifier.width(4.dp))

            Icon(
                painterResource(R.drawable.ic_dropdown),
                contentDescription = null,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEachIndexed { index, sortType ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                    onSortTypeSelected.invoke(sortType)
                }) {
                    Text(
                        text = stringResource(sortType.titleRes),
                        style = TextStyle(
                            fontFamily = Rubik,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 18.96.sp,
                        ),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSortDropdown() {
    ZveronTheme {
        SortDropdown()
    }
}