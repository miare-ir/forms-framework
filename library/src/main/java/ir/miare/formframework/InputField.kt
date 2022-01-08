package ir.miare.formframework

import android.view.View
import android.widget.EditText
import android.widget.TextView
import ir.miare.formframework.R
import ir.miare.formframeworkatters.Formatter
import ir.miare.formframework.utils.color
import ir.miare.formframework.utils.shake
import ir.miare.formframework.utils.stringify

open class InputField(
    private val editText: EditText,
    private val validator: Validator,
    private val formatter: Formatter,
    private val actionId: Int? = null,
    private val isText: Boolean = false,
    private val animationTarget: View = editText
) {
    private var wasValid = false
    private var evaluated = false
    private var attemptedToSubmit = false
    private val watcher: FieldWatcher by lazy {
        if (isText) {
            TextFieldWatcher(editText, ::validateInput)
        } else {
            NumberFieldWatcher(editText, ::validateFormatted)
        }
    }
    private var listener: InputFieldListener? = null
    private val context
        get() = editText.context

    fun isValid(): Boolean = wasValid

    fun attach(listener: InputFieldListener): FieldWatcher {
        this.listener = listener

        editText.addTextChangedListener(watcher)
        if (actionId != null) {
            editText.imeOptions = actionId
        }
        editText.setOnEditorActionListener(buildActionListener())

        watcher.afterTextChanged(editText.text)
        return watcher
    }

    fun detach() {
        evaluated = false
        editText.removeTextChangedListener(watcher)
        watcher.detach()
    }

    fun clean(): String? {
        if (wasValid) {
            try {
                val input = SelectableString(
                    editText.text.stringify(),
                    editText.selectionStart,
                    editText.selectionEnd
                )
                val rawInput = formatter.raw(input)
                return validator.clean(rawInput.value)
            } catch (e: IllegalArgumentException) {
                // Continue with invalid input
            }
        }

        attemptedToSubmit = true
        invalidated()
        shake()
        return null
    }

    private fun buildActionListener(): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { _, actionId, _ ->
            clean()

            if (actionId == this@InputField.actionId) {
                listener?.action()
                true
            } else {
                false
            }
        }
    }

    private fun validateFormatted(input: SelectableString): SelectableString {
        val rawInput = formatter.raw(input)
        validateInput(rawInput)
        return formatter.format(rawInput)
    }

    private fun validateInput(rawInput: SelectableString) {
        val cleanValue = try {
            validator.clean(rawInput.value)
        } catch (e: IllegalArgumentException) {
            null
        }
        val valid = cleanValue != null

        val validatedJustNow = (!evaluated || !wasValid) && valid
        val invalidatedJustNow = (!evaluated || wasValid) && !valid
        evaluated = true
        // Evaluated field is used to make sure that we invalidate invalid field first time it is
        // evaluated, other wise due to [wasValid && !valid] being false we do not invalidate it
        wasValid = valid

        if (validatedJustNow) {
            validated()
        } else if (invalidatedJustNow) {
            invalidated()
        }
    }

    private fun validated() {
        setNormalStyle()
        listener?.validated()
    }

    private fun invalidated() {
        if (attemptedToSubmit) {
            setErrorStyle()
        } else {
            setNormalStyle()
        }
        listener?.invalidated()
    }

    private fun shake() {
        animationTarget.shake()
    }

    private fun setNormalStyle() {
        editText.setBackgroundResource(NORMAL_BACKGROUND)
        editText.setTextColor(context.color(NORMAL_COLOR))
        editText.setHintTextColor(context.color(NORMAL_COLOR))
    }

    private fun setErrorStyle() {
        editText.setBackgroundResource(ERROR_BACKGROUND)
        editText.setTextColor(context.color(ERROR_COLOR))
        editText.setHintTextColor(context.color(ERROR_COLOR))
    }

    companion object {
         val NORMAL_BACKGROUND = R.drawable.selector_input_background
         val ERROR_BACKGROUND = R.drawable.bg_input_error
         val NORMAL_COLOR = R.color.primary
         val ERROR_COLOR = R.color.txtError
    }
}

interface InputFieldListener {
    fun validated()
    fun invalidated()
    fun action()
}
