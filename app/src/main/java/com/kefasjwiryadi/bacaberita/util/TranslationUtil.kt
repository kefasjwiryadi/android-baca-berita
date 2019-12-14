package com.kefasjwiryadi.bacaberita.util

import com.kefasjwiryadi.bacaberita.BacaBeritaApplication
import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.network.NewsApiService

fun String.translate(): String {
    return when (this) {
        NewsApiService.CATEGORY_BUSINESS -> BacaBeritaApplication.getString(R.string.business)
        NewsApiService.CATEGORY_ENTERTAINMENT -> BacaBeritaApplication.getString(R.string.entertainment)
        NewsApiService.CATEGORY_SPORTS -> BacaBeritaApplication.getString(R.string.sports)
        NewsApiService.CATEGORY_TECHNOLOGY -> BacaBeritaApplication.getString(R.string.technology)
        NewsApiService.CATEGORY_SCIENCE -> BacaBeritaApplication.getString(R.string.science)
        NewsApiService.CATEGORY_HEALTH -> BacaBeritaApplication.getString(R.string.health)
        else -> BacaBeritaApplication.getString(R.string.general)
    }
}