package com.kefasjwiryadi.bacaberita.util

import android.app.Activity
import android.content.Context
import androidx.core.app.ShareCompat
import com.kefasjwiryadi.bacaberita.domain.Article
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

fun String.clearUrl(): String {
    return clean("<")
}

fun String.cleanContent(): String {
    return clean("[")
}

fun String.cleanTitle(): String {
    val cleanedTitle = clean("-")
    if ((cleanedTitle.contains(".co") || cleanedTitle.contains("VIVA")) && cleanedTitle.contains("-")) {
        return cleanedTitle.clean("-")
    } else {
        return cleanedTitle
    }
}

private fun String.clean(pattern: String): String {
    val endIndex = this.lastIndexOf(pattern)
    return if (endIndex == -1) {
        this
    } else {
        this.substring(0, endIndex)
    }
}

fun String.toMillis(): Long {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
    val date = dateFormatter.parse(this)
    return date.time
}

fun String.toDateFormat(): String {
    val millis = this.toMillis()
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm 'WIB'")
    dateFormatter.timeZone = TimeZone.getTimeZone("GMT+7")
    return dateFormatter.format(Date(millis))
}

fun String.toPrettyTime(): String {
    val millis = this.toMillis()
    val p = PrettyTime(Locale("id"))
    return p.format(Date(millis))
}

fun Article.share(context: Context) {
    val sb = StringBuilder(title ?: "")
    content?.let {
        sb.append("\n\n$it")
    }
    sb.append("\n\nBaca selengkapnya di ${url.clearUrl()}")

    ShareCompat.IntentBuilder.from(context as Activity)
        .setType("text/plain")
        .setText(sb.toString())
        .startChooser()
}

