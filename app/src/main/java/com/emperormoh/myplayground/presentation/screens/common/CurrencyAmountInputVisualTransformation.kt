package com.emperormoh.myplayground.presentation.screens.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat

class CurrencyAmountInputVisualTransformation(
    private val fixedCursorAtTheEnd: Boolean = true,
    private val numberOfDecimals: Int = 2,
    private val currencySymbol: String = "₦"
): VisualTransformation {

    private val symbols = DecimalFormat().decimalFormatSymbols

    override fun filter(text: AnnotatedString): TransformedText {
        val thousandsSeparator = symbols.groupingSeparator
        val decimalSeparator = symbols.decimalSeparator
        val zero = symbols.zeroDigit

        val inputText = text.text
        println("amount inputText: $inputText")
        val intPart = if (inputText.isEmpty()) {
            zero.toString() // If input is empty, just display the zero
        } else {
            inputText
                .dropLast(numberOfDecimals) // Remove the fractional part
                .reversed() // Reverse to chunk the number in groups of three
                .chunked(3) // Split into groups of 3 digits
                .joinToString(thousandsSeparator.toString()) // Join them back with the thousands separator
                .reversed() // Reverse it back to the original order
                .ifEmpty {
                    zero.toString() // If there's no integer part, show zero
                }
        }
        println("amount int part: $intPart")
        val fractionPart = if (inputText.isEmpty()) {
            // If the input is empty, fill the fraction part with zeros
            List(numberOfDecimals) { zero }.joinToString("")
        } else {
            inputText.takeLast(numberOfDecimals).let {
                if (it.length != numberOfDecimals) {
                    // If the fraction part is too short, pad it with zeros
                    List(numberOfDecimals - it.length) {
                        zero
                    }.joinToString("") + it
                } else {
                    it
                }
            }
        }
        println("amount fraction part: $fractionPart")
        val formattedNumber = currencySymbol + intPart + decimalSeparator + fractionPart
        println("amount formattedNumber: $formattedNumber")
        val newText = AnnotatedString(
            text = if (text.text.isEmpty()) "" else formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        val offsetMapping = if (fixedCursorAtTheEnd) {
            FixedCursorOffsetMapping(
                contentLength = inputText.length,
                formattedContentLength = if(inputText.isNotEmpty()) formattedNumber.length else 0
            )
        } else {
            MovableCursorOffsetMapping(
                unmaskedText = text.toString(),
                maskedText = newText.toString(),
                decimalDigits = numberOfDecimals
            )
        }

        return TransformedText(newText, offsetMapping)
    }

    private class FixedCursorOffsetMapping(
        private val contentLength: Int,
        private val formattedContentLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = formattedContentLength
        override fun transformedToOriginal(offset: Int): Int = contentLength
    }

    private class MovableCursorOffsetMapping(
        private val unmaskedText: String,
        private val maskedText: String,
        private val decimalDigits: Int
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            when {
                unmaskedText.length <= decimalDigits -> {
                    maskedText.length - (unmaskedText.length - (offset))
                }

                else -> {
                    offset + offsetMaskCount(offset, removeExtraChar(maskedText, '.'))
                }
            }

        override fun transformedToOriginal(offset: Int): Int =
            when {
                unmaskedText.length <= decimalDigits -> {
                    Integer.max(unmaskedText.length - (maskedText.length - (offset)), 0)
                }

                else -> {
                    offset - maskedText.take(offset).count { !it.isDigit() }
                }
            }

        private fun offsetMaskCount(offset: Int, maskedText: String): Int {
            var maskOffsetCount = 0
            var dataCount = 0
            for (maskChar in maskedText) {
                if (!maskChar.isDigit()) {
                    maskOffsetCount++
                } else if (++dataCount > offset) {
                    break
                }
            }
            return maskOffsetCount
        }
    }
}

fun removeExtraChar(input: String, char: Char): String {
    val index = input.indexOf(char)
    return input.substring(0, index + 1) + input.substring(index + 1).replace(char.toString(), "")
}