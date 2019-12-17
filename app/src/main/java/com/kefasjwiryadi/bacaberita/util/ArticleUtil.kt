package com.kefasjwiryadi.bacaberita.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ShareCompat
import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.domain.Article
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

/**
 * Remove \n and \r from content.
 */
fun String.cleanContent(): String {
    var inWhiteSpace = false

    val sb = StringBuilder()
    forEachIndexed { i, c ->
        if (!c.isWhitespace()) {
            sb.append(c)
            inWhiteSpace = false
        } else if (!inWhiteSpace && c.isWhitespace()) {
            sb.append(" ")
            inWhiteSpace = true
        }
    }
    return sb.toString()
}

/**
 * Remove source name from title.
 */
fun String.cleanTitle(): String {
    val cleanedTitle = clean("-")
    return if ((cleanedTitle.contains(".co") || cleanedTitle.contains("VIVA")) && cleanedTitle.contains(
            "-"
        )
    ) {
        cleanedTitle.clean("-").trim()
    } else {
        cleanedTitle.trim()
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

/**
 * Clean title and content of an article.
 */
fun Article.clean() {
    title = title?.cleanTitle()
    content = content?.cleanContent()
}

/**
 * Clean title and content of a list of article.
 */
fun List<Article>.clean() {
    forEach {
        it.clean()
    }
}

/**
 * Convert ISO 8601 date format to millis.
 */
@SuppressLint("SimpleDateFormat")
fun String.toMillis(): Long {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
    val date = dateFormatter.parse(this)
    return date.time
}

/**
 * Convert ISO 8601 date format to readable date format.
 */
@SuppressLint("SimpleDateFormat")
fun String.toDateFormat(): String {
    val millis = this.toMillis()
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm 'WIB'")
    dateFormatter.timeZone = TimeZone.getTimeZone("GMT+7")
    return dateFormatter.format(Date(millis))
}

/**
 * Convert ISO 8601 date format to pretty time (X hours ago, X days ago, etc).
 */
fun String.toPrettyTime(): String {
    val millis = this.toMillis()
    val p = PrettyTime(Locale("id"))
    return p.format(Date(millis))
}

/**
 * Create share intent based on an article.
 */
fun Article.share(context: Context) {
    val sb = StringBuilder(title ?: "")
    description?.let {
        sb.append("\n\n$it")
    }
    sb.append("\n\n${context.getString(R.string.read_more_on, url)}")

    ShareCompat.IntentBuilder.from(context as Activity)
        .setType("text/plain")
        .setText(sb.toString())
        .startChooser()
}

/**
 * Parse full HTML of an article from a news site to get only the content.
 */
fun String.getContent(partialContent: String): String {
    val doc = Jsoup.parse(this)
    var paragraphs =
        doc.select("div:contains(${partialContent.substring(0, 30)})").last()
    if (paragraphs == null) {
        paragraphs = doc.select("div:contains(${partialContent.substring(30, 60)})").last()
    }

    paragraphs.select("div, style, table, script").remove()

    val sb = StringBuilder()

    return if (paragraphs.ownText().trim().isEmpty()) {
        paragraphs.children().forEach {
            if (it.shouldShow()) {
                sb.append(it.toString() + "\n")
            }
        }
        "$sb"
    } else {
        paragraphs.toString()
    }
}

private fun Element.shouldShow(): Boolean {
    return (text().isNotEmpty() && !text().contains('{')) || tagName() == "img"
}

fun Article.showPopupMenu(
    view: View,
    saveArticle: (article: Article) -> Unit,
    removeArticle: (article: Article) -> Unit
) {
    val menu = PopupMenu(view.context, view)
    menu.inflate(R.menu.article_item_menu)

    menu.menu.removeItem(
        if (favorite > 0) {
            R.id.save_action
        } else {
            R.id.remove_action
        }
    )

    menu.setOnMenuItemClickListener { menu ->
        when (menu.itemId) {
            R.id.share_action -> {
                share(view.context)
            }
            R.id.save_action -> {
                saveArticle(this)
                Toast.makeText(
                    view.context,
                    view.context.getString(R.string.saved_to_favorite),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            R.id.remove_action -> {
                removeArticle(this)
                Toast.makeText(
                    view.context,
                    view.context.getString(R.string.removed_from_favorite),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        return@setOnMenuItemClickListener true
    }
    menu.show()
}