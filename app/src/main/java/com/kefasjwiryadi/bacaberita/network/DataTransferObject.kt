package com.kefasjwiryadi.bacaberita.network

import com.kefasjwiryadi.bacaberita.domain.Article

/**
 * Simple object to hold News API responses.
 */
data class ArticlesNetworkContainer(
    val totalResults: Int,
    val articles: List<Article>
)
