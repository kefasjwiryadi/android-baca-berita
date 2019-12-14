package com.kefasjwiryadi.bacaberita.ui.favorite

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.repository.AppRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val appRepository: AppRepository) : ViewModel() {

    fun addFavoriteArticle(article: Article) {
        viewModelScope.launch {
            appRepository.addFavoriteArticle(article)
        }
    }

    fun removeFavoriteArticle(article: Article) {
        viewModelScope.launch {
            appRepository.removeFavoriteArticle(article)
        }
    }

    val favoriteArticles = appRepository.getFavoriteArticles()

    val isEmptyFavorite = Transformations.map(favoriteArticles) {
        return@map it.isNullOrEmpty()
    }
}

class FavoriteViewModelFactory(private val appRepository: AppRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FavoriteViewModel(appRepository) as T
    }

}