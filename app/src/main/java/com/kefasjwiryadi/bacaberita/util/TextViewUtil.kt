package com.kefasjwiryadi.bacaberita.util

import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView


fun TextView.setTextHtml(html: String) {
    val sequence = Html.fromHtml(html)
    val strBuilder = SpannableStringBuilder(sequence)
    val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
    for (span in urls) {
        makeLinkClickable(strBuilder, span)
    }
    text = strBuilder
    movementMethod = LinkMovementMethod.getInstance()
}

private fun makeLinkClickable(stringBuilder: SpannableStringBuilder, span: URLSpan) {
    val start: Int = stringBuilder.getSpanStart(span)
    val end: Int = stringBuilder.getSpanEnd(span)
    val flags: Int = stringBuilder.getSpanFlags(span)
    val clickable: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View?) {
            openWebsiteUrl(view!!.context, span.url)
        }
    }
    stringBuilder.setSpan(clickable, start, end, flags)
    stringBuilder.removeSpan(span)
}