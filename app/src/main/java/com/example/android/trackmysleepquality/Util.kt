package com.example.android.trackmysleepquality

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.android.trackmysleepquality.database.SleepNight
import java.text.SimpleDateFormat

/**
 * Estas funciones crean una cadena formateada que se puede configurar en un TextView.
 */

/**
 * Devuelve una cadena que representa la calificación de calidad numérica.
 */
fun convertNumericQualityToString(quality: Int, resources: Resources): String {
    var qualityString = resources.getString(R.string.three_ok)
    when (quality) {
        -1 -> qualityString = "--"
        0 -> qualityString = resources.getString(R.string.zero_very_bad)
        1 -> qualityString = resources.getString(R.string.one_poor)
        2 -> qualityString = resources.getString(R.string.two_soso)
        4 -> qualityString = resources.getString(R.string.four_pretty_good)
        5 -> qualityString = resources.getString(R.string.five_excellent)
    }
    return qualityString
}


/**
 * Tomar los milisegundos largos devueltos por el sistema y almacenados en Room,
 * y convertirlo en una cadena bien formateada para su visualización.
 *
 * EEEE: muestra la versión de letra larga del día de la semana
 * MMM - Muestra la abreviatura de la letra del nmotny
 * dd-yyyy - día en mes y año completo numéricamente
 * HH:mm - Horas y minutos en formato de 24 horas
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
            .format(systemTime).toString()
}

/**
 * Toma una lista de SleepNights y la convierte y la formatea en una cadena para mostrarla.
 *
 * Para mostrar en un TextView, tenemos que proporcionar una cadena, y los estilos son por TextView, no
 * aplicable por palabra. Entonces, construimos una cadena formateada usando HTML. Esto es útil, pero lo haremos
 * aprenda una mejor manera de mostrar estos datos en una lección futura.
 *
 * @param nights - Lista de todos los SleepNights en la base de datos.
 * @param resources: objeto de recursos para todos los recursos definidos para nuestra aplicación.
 *
 * @return Spanned: una interfaz para texto que tiene formato adjunto.
 * Ver: https://developer.android.com/reference/android/text/Spanned
 */
fun formatNights(nights: List<SleepNight>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.title))
        nights.forEach {
            append("<br>")
            append(resources.getString(R.string.start_time))
            append("\t${convertLongToDateString(it.startTimeMilli)}<br>")
            if (it.endTimeMilli != it.startTimeMilli) {
                append(resources.getString(R.string.end_time))
                append("\t${convertLongToDateString(it.endTimeMilli)}<br>")
                append(resources.getString(R.string.quality))
                append("\t${convertNumericQualityToString(it.sleepQuality, resources)}<br>")
                append(resources.getString(R.string.hours_slept))
                // Hours
                append("\t ${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60 / 60}:")
                // Minutes
                append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60}:")
                // Seconds
                append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000}<br><br>")
            }
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
