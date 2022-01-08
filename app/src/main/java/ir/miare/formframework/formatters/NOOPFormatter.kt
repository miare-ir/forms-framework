package ir.miare.formframework.formatters

import ir.miare.formframework.SelectableString
import ir.miare.formframeworkatters.Formatter

class NOOPFormatter : Formatter {
    override fun raw(input: SelectableString) = input

    override fun format(input: SelectableString) = input
}
