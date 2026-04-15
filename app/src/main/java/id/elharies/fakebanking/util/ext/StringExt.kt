package id.elharies.fakebanking.util.ext

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(format: String, locale: Locale = Locale.getDefault()): Date {
    return try {
        SimpleDateFormat(format, locale).parse(this) ?: Date()
    } catch (e: Exception) {
        Date()
    }
}

fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    }
}