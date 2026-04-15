package id.elharies.fakebanking.util.ext

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toString(f: String, locale: Locale = Locale.getDefault()): String {
    val simpleDateFormat = SimpleDateFormat(f, locale)
    return simpleDateFormat.format(this)
}