package ir.miare.formframework

import android.widget.Button
import ir.miare.formframework.utils.onClick

open class BaseFormController(
    private val inputs: List<InputField>,
    private val button: Button,
    private val submitter: FormSubmitter
) : InputFieldListener {

    var vibrator: ErrorVibrator = ErrorVibrator()

    // TODO: Attaching an instance of this class after detaching it causes ANRs
    //  (probably multiple watchers being attached to an EditText).
    //  Just detach codeController in LoginActivity before attaching it to reproduce it
    fun attach() {
        inputs.forEach {
            it.attach(this)
        }
        button.onClick { submit() }
    }

    fun detach() {
        for (validatedEditText in inputs) {
            validatedEditText.detach()
        }
        button.setOnClickListener(null)
    }

    private fun submit() {
        val values = inputs.map { it.clean() }
        val valid = values.none { it == null }

        if (valid) {
            submitter.submit(values.filterNotNull()) // Conversion is done to satisfy null-safety
        } else {
            vibrate()
        }
    }

    private fun vibrate() {
        val context = button.context ?: return
        vibrator.vibrate(context)
    }

    override fun validated() {
        if (isValid()) {
            button.isActivated = true
        }
    }

    override fun invalidated() {
        button.isActivated = false
    }

    override fun action() {
        submit()
    }

    private fun isValid(): Boolean = inputs.all { it.isValid() }
}

interface FormSubmitter {
    fun submit(values: List<String>)
}
