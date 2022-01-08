package ir.miare.formframeworkatters

import ir.miare.formframework.SelectableString

interface Formatter {
    fun raw(input: SelectableString): SelectableString
    fun format(input: SelectableString): SelectableString
}
