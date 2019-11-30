package com.kefasjwiryadi.bacaberita.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kefasjwiryadi.bacaberita.repository.AppRepository

class FavoriteViewModel(appRepository: AppRepository) : ViewModel() {
    val favoriteArticles = appRepository.getFavoriteArticles()
}

class FavoriteViewModelFactory(private val appRepository: AppRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoriteViewModel(appRepository) as T
    }

}