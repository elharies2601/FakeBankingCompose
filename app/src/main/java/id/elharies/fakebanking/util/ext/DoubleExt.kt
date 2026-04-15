package id.elharies.fakebanking.util.ext

import android.icu.text.NumberFormat
import java.util.Locale

fun Double.formatRupiah(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(this).replace("Rp", "Rp ").replace(",00", "")
}