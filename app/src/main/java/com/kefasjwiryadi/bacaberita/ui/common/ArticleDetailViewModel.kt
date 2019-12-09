package com.kefasjwiryadi.bacaberita.ui.common

import android.util.Log
import androidx.lifecycle.*
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.network.Status
import com.kefasjwiryadi.bacaberita.repository.AppRepository
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val appRepository: AppRepository,
    private val article: Article
) : ViewModel() {

    val articleLd = appRepository.getArticleLd(article.url)

    private val _eventToast = MutableLiveData<String>("")
    val eventToast: LiveData<String>
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
        return ArticleDetailViewModel(appRepository, article) as T
    }
}