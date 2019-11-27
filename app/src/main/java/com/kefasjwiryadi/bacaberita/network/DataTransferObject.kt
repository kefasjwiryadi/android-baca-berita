package com.kefasjwiryadi.bacaberita.network

import com.kefasjwiryadi.bacaberita.domain.Article

data class ArticlesNetworkContainer(
    val totalResults: Int,
    val articles: List<Article>
)
