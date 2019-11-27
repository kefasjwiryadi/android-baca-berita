package com.kefasjwiryadi.bacaberita.util

import com.kefasjwiryadi.bacaberita.network.NewsApiService

fun String.translate(): String {
    return when (this) {
        NewsApiService.CATEGORY_BUSINESS -> "bisnis"
        NewsApiService.CATEGORY_ENTERTAINMENT -> "hiburan"
        NewsApiService.CATEGORY_SPORTS -> "olahraga"
        NewsApiService.CATEGORY_TECHNOLOGY -> "teknologi"
        NewsApiService.CATEGORY_SCIENCE -> "sains"
        NewsApiService.CATEGORY_HEALTH -> "kesehatan"
        else -> "umum"
    }
}