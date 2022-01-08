package ir.miare.formframework

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import timber.log.Timber

class ErrorVibrator {
    fun vibrate(context: Context?) {
        if (context == null) {
            Timber.d("Cannot vibrate, context is null")
            return
        }

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(FORM_DURATION_MILLIS, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(FORM_DURATION_MILLIS)
        }
    }

    companion object {
        private const val FORM_DURATION_MILLIS = 150L
    }
}
