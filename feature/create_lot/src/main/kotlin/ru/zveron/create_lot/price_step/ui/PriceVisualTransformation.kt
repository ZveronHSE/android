package ru.zveron.create_lot.price_step.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.min

class PriceVisualTransformation(
    private val suffix: String
): VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val annotatedString = buildAnnotatedString {
            append(text)

            append(suffix)
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                return min(offset, text.length)
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }
}