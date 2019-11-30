package com.kefasjwiryadi.bacaberita.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.repository.AppRepository

class ArticleDetailViewModel(
    private val appRepository: AppRepository,
    private val article: Article
) : ViewModel() {

//    fun saveArticle() {
//        if (isArticleFavorited.value == false) {
//            viewModelScope.launch {
//                appRepository.addArticleToFavorite(article)
//            }
//        } else {
//            viewModelScope.launch {
//                appRepository.deleteArticleFromFavorite(article)
//            }
//        }
//    }
//
//    val isArticleFavorited = appRepository.isArticleFavorited(article)
}

class ArticleDetailViewModelFactory(
    private val appRepository: AppRepository,
    private val article: Article
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleDetailViewModel(appRepository, article) as T
    }
}