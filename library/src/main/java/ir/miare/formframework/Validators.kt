package ir.miare.formframework

import ir.miare.formframework.utils.PhoneNumberUtils
import ir.miare.formframework.utils.withEnglishDigits

interface Validator {
    @Throws(IllegalArgumentException::class)
    fun clean(input: String): String
}

open class NumberValidator(
    private val min: Int = Int.MIN_VALUE,
    private val max: Int = Int.MAX_VALUE
) : Validator {
    override fun clean(input: String): String {
        val numberString = input.withEnglishDigits()
        val number = numberString.toInt()

        if (number < min || number > max) {
            throw IllegalArgumentException("$number does not reside between $min and $max")
        }
        return number.toString()
    }
}

open class PriceValidator(
    private val acceptZero: Boolean = true,
    private val min: Int = Int.MIN_VALUE,
    private val max: Int = Int.MAX_VALUE
) : Validator {
    override fun clean(input: String): String {
        val numberString = input.withEnglishDigits()
        val number = numberString.toInt()

        if (acceptZero && number == 0) {
            return number.toString()
        }
        if (number < min || number > max) {
            throw IllegalArgumentException("$number does not reside between $min and $max")
        }
        return number.toString()
    }
}

class MobileNumberValidator : Validator {
    override fun clean(input: String): String = PhoneNumberUtils.validateMobileNumber(input)
}

class PhoneNumberValidator : Validator {
    override fun clean(input: String): String = PhoneNumberUtils.validatePhoneNumber(input)
}

open class LengthValidator(
    private val min: Int = Int.MIN_VALUE,
    private val max: Int = Int.MAX_VALUE
) : Validator {
    override fun clean(input: String): String {
        if (input.length < min || input.length > max) {
            throw IllegalArgumentException(
                "$input's length (${input.length}) does not reside between $min and $max"
            )
        }
        return input
    }
}

class ShebaDigitsValidator : LengthValidator(SHEBA_DIGITS_LENGTH, SHEBA_DIGITS_LENGTH) {
    override fun clean(input: String): String {
        val digitsString = input.withEnglishDigits()
        if (digitsString.any { !it.isDigit() }) {
            throw IllegalArgumentException("$digitsString contains non-digit characters")
        }
        return super.clean(digitsString)
    }

    private companion object {
        private const val SHEBA_DIGITS_LENGTH = 24
    }
}

class NonEmptyValidator : Validator {
    override fun clean(input: String): String {
        if (input.isEmpty() || input.isBlank()) {
            throw IllegalArgumentException("$input is empty or blank")
        }
        return input
    }
}
