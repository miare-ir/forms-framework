package ir.miare.formframeworkatters

import ir.miare.formframework.SelectableString

class CurrencyFormatter : Formatter {
    override fun raw(input: SelectableString): SelectableString {
        var newStart = input.start
        var newEnd = input.start
        var newValue = input.value

        var firstDelimiter = newValue.indexOf('٫')
        while (firstDelimiter > -1) {
            newValue = newValue.replaceRange(firstDelimiter, firstDelimiter + 1, "")
            if (firstDelimiter < newStart) {
                newStart--
            }
            if (firstDelimiter < newEnd) {
                newEnd--
            }

            firstDelimiter = newValue.indexOf('٫')
        }

        return SelectableString(newValue, newStart, newEnd)
    }

    override fun format(input: SelectableString): SelectableString {
        try {
            input.value.toInt()
        } catch (e: IllegalArgumentException) {
            return input
        }

        var newStart = input.start
        var newEnd = input.start
        val valueBuilder = StringBuilder(input.value)

        if (valueBuilder[0] == '0' && valueBuilder.length > 1) {
            valueBuilder.deleteCharAt(0)
            if (newStart > 0) {
                newStart--
            }
            if (newEnd > 0) {
                newEnd--
            }
        }

        var delimiterPosition = 3
        while (valueBuilder.length > delimiterPosition) {
            val length = valueBuilder.length
            valueBuilder.insert(length - delimiterPosition, '٫')
            if (length - delimiterPosition < newStart) {
                newStart++
            }
            if (length - delimiterPosition < newEnd) {
                newEnd++
            }
            delimiterPosition += 4
        }

        return SelectableString("$valueBuilder", newStart, newEnd)
    }
}
