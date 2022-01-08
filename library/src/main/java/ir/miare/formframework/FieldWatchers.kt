package ir.miare.formframework

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

abstract class FieldWatcher(protected var editText: EditText?) : TextWatcher {
    final override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        /* Do nothing */
    }

    final override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        /* Do nothing */
    }

    final override fun afterTextChanged(s: Editable) {
        changeText(s)
    }

    protected abstract fun changeText(editable: Editable)

    fun detach() {
        editText = null
    }
}

class NumberFieldWatcher(
    editText: EditText,
    private val clean: (SelectableString) -> SelectableString
) : FieldWatcher(editText) {
    override fun changeText(editable: Editable) {
        val cleanInput = clean(SelectableString.fromEditable(editable))
        editText?.removeTextChangedListener(this)
        cleanInput.applyOnEditable(editable)
        editText?.addTextChangedListener(this)
    }
}

class TextFieldWatcher(editText: EditText, private val validate: (SelectableString) -> Unit) :
    FieldWatcher(editText) {
    override fun changeText(editable: Editable) {
        validate(SelectableString.fromEditable(editable))
    }
}
