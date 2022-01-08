package ir.miare.formframework.utils

object PhoneNumberUtils {
    private const val PHONE_NUMBER_STANDARD_LENGTH_WITH_ZERO = 11

    /**
     * Turns the given phone number into a standard form
     *
     *
     * Supported input formats are:
     * - 09123456789
     * - 9123456789
     * - +989123456789
     * - 00989123456789
     * - 989123456789
     *
     *
     * Output is either 912345678 or 0912345678 based on whether or not firstZero is true
     */
    private fun normalizePhoneNumber(input: String?, firstZero: Boolean = true): String {
        input ?: return ""

        var number = DigitUtils.toEnglishDigits(input)
        number = number.replace(" ".toRegex(), "")
        number = when {
            number.startsWith("989") -> number.substring("98".length)
            number.startsWith("00989") -> number.substring("0098".length)
            number.startsWith("+989") -> number.substring("+98".length)
            number.startsWith("+9") -> number.substring("+".length)
            number.startsWith("09") -> number.substring("0".length)
            else -> number
        }

        if (firstZero) {
            number = "0$number"
        }

        number.forEach { char ->
            if (!Character.isDigit(char)) {
                return ""
            }
        }
        return number
    }

    @Throws(IllegalArgumentException::class)
    fun validateMobileNumber(phoneNumber: String): String {
        val normalizedPhoneNumber = normalizePhoneNumber(phoneNumber)
        return if (normalizedPhoneNumber.length == PHONE_NUMBER_STANDARD_LENGTH_WITH_ZERO && normalizedPhoneNumber.startsWith("09")) {
            normalizedPhoneNumber
        } else {
            throw IllegalArgumentException("$phoneNumber is not a valid phone number")
        }
    }

    @Throws(IllegalArgumentException::class)
    fun validatePhoneNumber(phoneNumber: String): String {
        val normalizedPhoneNumber = normalizePhoneNumber(phoneNumber)
        return if (normalizedPhoneNumber.length == PHONE_NUMBER_STANDARD_LENGTH_WITH_ZERO) {
            normalizedPhoneNumber
        } else {
            throw IllegalArgumentException("$phoneNumber is not a valid phone number")
        }
    }
}
