package com.kefasjwiryadi.bacaberita.domain

data class ArticleSearchResult(
    val query: String,
    val articles: List<Article>,
    val totalResult: Int,
    val page: Int
)