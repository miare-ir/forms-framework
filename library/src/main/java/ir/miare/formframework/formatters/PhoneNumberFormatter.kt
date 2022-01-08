package ir.miare.formframework.formatters

import ir.miare.formframework.SelectableString
import ir.miare.formframeworkatters.Formatter

class PhoneNumberFormatter : Formatter {
    override fun raw(input: SelectableString): SelectableString {
        var newStart = input.start
        var newEnd = input.start
        var newValue = input.value

        var firstSpace = newValue.indexOf(' ')
        while (firstSpace > -1) {
            newValue = newValue.replaceRange(firstSpace, firstSpace + 1, "")
            if (firstSpace < newStart) {
                newStart--
            }
            if (firstSpace < newEnd) {
                newEnd--
            }

            firstSpace = newValue.indexOf(' ')
        }

        return SelectableString(newValue, newStart, newEnd)
    }

    override fun format(input: SelectableString): SelectableString {
        var newStart = input.start
        var newEnd = input.start
        val valueBuilder = StringBuilder(input.value)

        val delimiters = listOf(4, 8, 11)
        for (delimiter in delimiters) {
            if (valueBuilder.length <= delimiter) {
                // Ignore short phone number
                break
            }

            valueBuilder.insert(delimiter, ' ')
            if (delimiter < newStart) {
                newStart++
            }
            if (delimiter < newEnd) {
                newEnd++
            }
        }

        return SelectableString("$valueBuilder", newStart, newEnd)
    }
}
