package ir.miare.formframework.utils

object DigitUtils {
    private val PERSIAN_DIGITS = listOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    private val ARABIC_DIGITS = listOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    private val LATIN_DIGITS = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    fun toEnglishDigits(input: String): String {
        var result = input
        (LATIN_DIGITS.indices).forEach { index ->
            result = result
                .replace(PERSIAN_DIGITS[index], LATIN_DIGITS[index])
                .replace(ARABIC_DIGITS[index], LATIN_DIGITS[index])
        }
        return result
    }

    fun toPersianDigits(input: String): String {
        var result = input
        (LATIN_DIGITS.indices).forEach { index ->
            result = result
                .replace(LATIN_DIGITS[index], PERSIAN_DIGITS[index])
                .replace(ARABIC_DIGITS[index], PERSIAN_DIGITS[index])
        }
        return result
    }
}
