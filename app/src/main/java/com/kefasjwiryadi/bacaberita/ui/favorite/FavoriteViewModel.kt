package com.kefasjwiryadi.bacaberita.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kefasjwiryadi.bacaberita.repository.AppRepository
import com.kefasjwiryadi.bacaberita.ui.search.SearchViewModel

class FavoriteViewModel(appRepository: AppRepository) : ViewModel() {
    val favoriteArticles = appRepository.getFavorites()
}

class FavoriteViewModelFactory(private val appRepository: AppRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoriteViewModel(appRepository) as T
    }

}