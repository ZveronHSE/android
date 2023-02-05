package ru.zveron.design.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.R
import ru.zveron.design.fonts.Gilroy
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.ZveronTheme

@Immutable
data class BottomNavigationItem(
    val image: ZveronImage,

    val title: ZveronText,

    val isSelected: Boolean,

    val onClick: () -> Unit,
)

private val BottomNavigationHeight = 64.dp

@Composable
fun BottomNavigation(
    items: List<BottomNavigationItem>,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = BottomNavigationDefaults.Elevation,
) {
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(fontFamily = Gilroy)
    ) {
        Surface(
            color = backgroundColor,
            contentColor = contentColor,
            elevation = elevation,
            modifier = modifier.clip(RoundedCornerShape(24.dp)),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(BottomNavigationHeight)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (item in items) {
                    BottomNavigationItem(
                        selected = item.isSelected,
                        onClick = item.onClick,
                        icon = {
                            ZveronImage(item.image)
                        },
                        label = {
                            ZveronText(
                                item.title,
                                style = LocalTextStyle.current.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 10.sp,
                                    lineHeight = 12.13.sp,
                                    letterSpacing = (-0.24).sp,
                                    fontFamily = Gilroy,
                                )
                            )
                        },
                        selectedContentColor = Color(0xFF18191B),
                        unselectedContentColor = Color(0xFF666770),
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.BottomNavigationItem(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selectedContentColor: Color,
    unselectedContentColor: Color,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val styledLabel: @Composable (() -> Unit) = label.let {
        @Composable {
            val style = MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(style, content = label)
        }
    }

    val ripple = rememberRipple(bounded = false, color = selectedContentColor)

    val contentColor = if (selected) selectedContentColor else unselectedContentColor
    CompositionLocalProvider(
        LocalContentColor provides contentColor
    ) {
        Column(
            modifier
                .selectable(
                    selected = selected,
                    onClick = onClick,
                    enabled = enabled,
                    role = Role.Tab,
                    interactionSource = interactionSource,
                    indication = ripple,
                )
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(Modifier.layoutId("icon")) {
                icon()
            }
            
            Spacer(Modifier.height(4.dp))

            Box(
                Modifier
                    .layoutId("label")) {
                styledLabel()
            }
        }
    }
}

@Preview
@Composable
fun BottomNavigationPreview() {
    val items = listOf(
        BottomNavigationItem(
            title = ZveronText.RawString("Главная"),
            image = ZveronImage.ResourceImage(R.drawable.heart_unliked),
            isSelected = false,
            onClick = {},
        ),
        BottomNavigationItem(
            title = ZveronText.RawString("Избранное"),
            image = ZveronImage.ResourceImage(R.drawable.heart_liked),
            isSelected = true,
            onClick = {},
        ),
        BottomNavigationItem(
            title = ZveronText.RawString("Объявления"),
            image = ZveronImage.ResourceImage(R.drawable.heart_unliked),
            isSelected = false,
            onClick = {},
        ),
        BottomNavigationItem(
            title = ZveronText.RawString("Сообщения"),
            image = ZveronImage.ResourceImage(R.drawable.heart_unliked),
            isSelected = false,
            onClick = {},
        ),
        BottomNavigationItem(
            title = ZveronText.RawString("Профиль"),
            image = ZveronImage.ResourceImage(R.drawable.heart_unliked),
            isSelected = false,
            onClick = {},
        ),
    )

    ZveronTheme {
        BottomNavigation(
            items = items,
        )
    }
}
