package com.kefasjwiryadi.bacaberita.ui.common

import androidx.lifecycle.*
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val appRepository: AppRepository,
    private val article: Article
) : ViewModel() {

    val articleLd = appRepository.getArticleLd(article.url)

    private val _eventToast = MutableLiveData<String>("")
    val eventToast: LiveData<String>
        get() = _eventToast

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (appRepository.getArticle(article.url) == null) {
                appRepository.insertArticle(article)
            }
        }
    }

    fun saveArticle() {
        if ((articleLd.value?.favorite ?: 0L) == 0L) {
            viewModelScope.launch {
                appRepository.addFavoriteArticle(article)
                _eventToast.value = "Disimpan ke favorit"
            }
        } else {
            viewModelScope.launch {
                appRepository.removeFavoriteArticle(article)
                _eventToast.value = "Dihapus dari favorit"
            }
        }
    }

    fun doneToast() {
        _eventToast.value = ""
    }
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