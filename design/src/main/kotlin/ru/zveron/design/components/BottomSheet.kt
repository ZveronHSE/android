package ru.zveron.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(MaterialTheme.colors.surface),
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .background(
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(50),
                )
                .size(width = 32.dp, height = 4.dp)
                .align(Alignment.CenterHorizontally)
        )

        content()
    }
}

@Preview
@Composable
private fun BottomSheetPreview() {
    BottomSheet {
        Text(
            text = "Hello bottom sheet!",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}