package com.kefasjwiryadi.bacaberita.ui.common

import android.util.Log
import androidx.lifecycle.*
import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.network.Status
import com.kefasjwiryadi.bacaberita.repository.AppRepository
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val appRepository: AppRepository,
    private val article: Article
) : ViewModel() {

    val articleLd = appRepository.getArticleLd(article.url)

    private val _eventToast = MutableLiveData(0)
    val eventToast: LiveData<Int>
        get() = _eventToast

    private val _status = MutableLiveData<Int>(Status.IDLE)
    val status: MutableLiveData<Int>
        get() = _status

    init {
        viewModelScope.launch {
            if (appRepository.getArticle(article.url) == null) {
                appRepository.insertArticle(article)
            }
            if (!article.content.isNullOrEmpty() && article.fullContent.isNullOrEmpty()) {
                getFullContent()
            }
        }
    }

    fun getFullContent() {
        viewModelScope.launch {
            _status.value = Status.LOADING
            try {
                appRepository.getFullContent(article)
                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                Log.d(TAG, "$e")
                _status.value = Status.FAILURE
            }
        }
    }

    fun saveArticle() {
        if ((articleLd.value?.favorite ?: 0L) == 0L) {
            viewModelScope.launch {
                appRepository.addFavoriteArticle(article)
                _eventToast.value = R.string.saved_to_favorite
            }
        } else {
            viewModelScope.launch {
                appRepository.removeFavoriteArticle(article)
                _eventToast.value = R.string.removed_from_favorite
            }
        }
    }

    fun doneToast() {
        _eventToast.value = 0
    }

    companion object {
        private const val TAG = "ArticleDetailViewModel"
    }
}

class ArticleDetailViewModelFactory(
    private val appRepository: AppRepository,
    private val article: Article
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ArticleDetailViewModel(appRepository, article) as T
    }
}