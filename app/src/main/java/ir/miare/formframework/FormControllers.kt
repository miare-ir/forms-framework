package ir.miare.formframework

import android.widget.Button
import android.widget.EditText
import ir.miare.formframework.formatters.NOOPFormatter
import ir.miare.formframework.formatters.PhoneNumberFormatter

open class SingleFieldFormController(
    field: InputField,
    button: Button,
    submit: (String) -> Unit
) : BaseFormController(
    listOf(field),
    button,
    object : FormSubmitter {
        override fun submit(values: List<String>) {
            submit(values[0])
        }
    }
)

class PhoneNumberFormController(
    editText: EditText,
    button: Button,
    actionId: Int,
    submit: (String) -> Unit
) : SingleFieldFormController(
    InputField(editText, PhoneNumberValidator(), PhoneNumberFormatter(), actionId),
    button, submit
)

class CodeFormController(
    editText: EditText,
    button: Button,
    actionId: Int,
    submit: (String) -> Unit
) : SingleFieldFormController(
    InputField(editText, NumberValidator(1000, 9999), NOOPFormatter(), actionId), button, submit
)
