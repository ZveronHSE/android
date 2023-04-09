package ru.zveron.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zveron.design.R
import ru.zveron.design.theme.ZveronTheme

@Composable
fun Stars(
    currentStars: Int,
    maxStars: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Row(
        modifier = modifier.semantics(mergeDescendants = true){
            contentDescription?.let {
                this.contentDescription = contentDescription
            }
        },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(currentStars) {
            Icon(
                painterResource(R.drawable.ic_star_active),
                null,
                tint = Color.Unspecified,
            )
        }

        repeat(maxStars - currentStars) {
            Icon(
                painterResource(R.drawable.ic_star_disabled),
                null,
                tint = Color.Unspecified,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StarsPreview() {
    ZveronTheme {
        Stars(currentStars = 4, maxStars = 5)
    }
}