package ir.miare.formframework

import android.text.Editable
import android.text.Selection

data class SelectableString(val value: String, val start: Int, val end: Int) {
    fun applyOnEditable(editable: Editable?) {
        editable ?: return

        editable.clear()
        editable.insert(0, value)
        Selection.setSelection(editable, start, end)
    }

    override fun toString(): String {
        return StringBuilder(value).insert(start, "|").insert(end, "|").toString()
    }

    companion object {
        fun fromEditable(editable: Editable?): SelectableString {
            if (editable == null) {
                return SelectableString("", 0, 0)
            }

            val value = editable.toString()
            val selectionStart = Selection.getSelectionStart(editable)
            val selectionEnd = Selection.getSelectionEnd(editable)
            require(selectionStart <= selectionEnd)

            return SelectableString(value, selectionStart, selectionEnd)
        }
    }
}
