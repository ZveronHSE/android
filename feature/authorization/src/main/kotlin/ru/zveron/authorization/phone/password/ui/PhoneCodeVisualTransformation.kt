package ru.zveron.authorization.phone.password.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.min

private const val PHONE_NUMBER_LENGTH = 10

class PhoneCodeVisualTransformation(
    val prefix: String,
): VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= PHONE_NUMBER_LENGTH) {
            text.subSequence(0, PHONE_NUMBER_LENGTH)
        } else {
            text
        }

        val annotatedString = buildAnnotatedString {
            append(prefix)

            append(trimmed)
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset + prefix.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                val offsetWithoutPrefix = offset - prefix.length
                if (offsetWithoutPrefix < 0) {
                    return 0
                }
                return min(offsetWithoutPrefix, text.length)
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }
}