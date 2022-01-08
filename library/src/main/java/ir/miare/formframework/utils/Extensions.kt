package ir.miare.formframework.utils

import android.content.Context
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo


fun <T : View> T.onClick(f: (T) -> Unit) = apply {
    setOnClickListener { f(this) }
}

fun Any?.stringify(default: String = "") = this?.let { "$it" } ?: default

fun String.withEnglishDigits(): String = DigitUtils.toEnglishDigits(this)

fun View.shake() {
    YoYo.with(Techniques.Shake)
        .duration(500)
        .playOn(this)
}

@ColorInt
fun Context.color(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)