package ru.zveron.design.resources

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

sealed class ZveronText {
    @Composable
    abstract fun getText(): String

    data class RawResource(@StringRes val resId: Int): ZveronText() {
        @Composable
        override fun getText(): String {
            return stringResource(id = resId)
        }
    }

    data class ArgResource(
        @StringRes val resId: Int,
        val formatArgs: List<Any>,
    ): ZveronText() {
        constructor(@StringRes resId: Int, vararg formatArgs: Any) : this(resId, formatArgs.toList())

        @Composable
        override fun getText(): String {
            return stringResource(resId, *formatArgs.toTypedArray())
        }
    }

    data class PluralResource(
        @PluralsRes val resId: Int,
        val plural: Int
    ): ZveronText() {
        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        override fun getText(): String {
            return pluralStringResource(id = resId, count = plural)
        }
    }

    data class PluralArgResource(
        @PluralsRes val resId: Int,
        val plural: Int,
        val formatArgs: List<Any>,
    ): ZveronText() {
        constructor(@PluralsRes resId: Int, plural: Int, vararg formatArgs: Any) : this(resId, plural, formatArgs.toList())

        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        override fun getText(): String {
            return pluralStringResource(id = resId, count = plural, *formatArgs.toTypedArray())
        }
    }

    data class RawString(val text: String): ZveronText() {
        @Composable
        override fun getText(): String {
            return text
        }
    }
}

@Composable
fun ZveronText(
    text: ZveronText,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        text.getText(),
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style,
    )
}