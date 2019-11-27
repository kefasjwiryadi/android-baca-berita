package com.kefasjwiryadi.bacaberita.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.kefasjwiryadi.bacaberita.R

private const val CHROME_PACKAGE = "com.android.chrome"

@BindingAdapter("websiteLink", "hideWhenEmpty", requireAll = false)
fun websiteLink(
    button: View,
    url: String?,
    hideWhenEmpty: Boolean = false
) {
    if (url.isNullOrEmpty()) {
        if (hideWhenEmpty) {
            button.isVisible = false
        } else {
            button.isClickable = false
        }
        return
    }
    button.isVisible = true
    button.setOnClickListener {
        openWebsiteUrl(it.context, url)
    }
}

fun openWebsiteUrl(context: Context, url: String) {
    if (url.isBlank()) {
        return
    }
    openWebsiteUri(context, Uri.parse(url))
}

fun openWebsiteUri(context: Context, uri: Uri) {
    if (context.isChromeCustomTabsSupported()) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .build()
        customTabsIntent.intent.putExtra(CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING, false)
        customTabsIntent.launchUrl(context, uri)
    } else {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}

private fun Context.isChromeCustomTabsSupported(): Boolean {
    val serviceIntent = Intent(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION)
    serviceIntent.setPackage(CHROME_PACKAGE)
    val resolveInfos = packageManager.queryIntentServices(serviceIntent, 0)
    return !resolveInfos.isNullOrEmpty()
}