package com.kefasjwiryadi.bacaberita.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.network.NewsApiService.Companion.PAGE_SIZE_DEF_VALUE
import com.kefasjwiryadi.bacaberita.repository.AppRepository
import com.kefasjwiryadi.bacaberita.util.Status
import com.kefasjwiryadi.bacaberita.util.isNotLoading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ArticleViewModel"

const val MINUTE_TO_MILLIS = 60L * 1000L
const val REFRESH_RATE = 30L * MINUTE_TO_MILLIS

class ArticleViewModel(
    private val appRepository: AppRepository,
    private val category: String
) : ViewModel() {

    val articles = appRepository.getArticles(category)

    private val _status = MutableLiveData<Int>(Status.IDLE)
    val status: LiveData<Int>
        get() = _status

    private val _eventResetList = MutableLiveData<Boolean>()
    val eventResetList: LiveData<Boolean>
        get() = _eventResetList

    private var loadMoreAllowed = false

    init {
        Log.d(TAG, "init: $category")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: $category")
    }

    fun onFragmentResume() {
        viewModelScope.launch(Dispatchers.IO) {
            if (shouldRefresh()) {
                refreshArticles()
            }
        }
    }

    fun onReachEnd() {
        viewModelScope.launch {
            if (loadMoreAllowed && _status.value!!.isNotLoading() && hasNextPage()) {
                val currentResult = appRepository.getArticleFetchResult(category) ?: return@launch
                val nextPage = currentResult.page + 1
                Log.d(TAG, "onReachEnd: $nextPage")
                if (_status.value == Status.LOADING_MORE) return@launch
                try {
                    _status.value = Status.LOADING_MORE
                    appRepository.loadNextPage(category, nextPage)
                    _status.value = Status.SUCCESS
                } catch (e: Exception) {
                    _status.value = Status.FAILURE
                    Log.d(TAG, "onReachEnd: $e")
                }
            }
        }
    }

    private suspend fun hasNextPage(): Boolean {
        val currentResult = appRepository.getArticleFetchResult(category) ?: return false
        return currentResult.page * PAGE_SIZE_DEF_VALUE < currentResult.totalResult
    }

    private suspend fun shouldRefresh(): Boolean {
        val currentResult = appRepository.getArticleFetchResult(category) ?: return true
        val firstRetrievedMillis = currentResult.firstRetrieved
        val timeDiff = System.currentTimeMillis() - firstRetrievedMillis
        return if (timeDiff > REFRESH_RATE) {
            Log.d(
                TAG,
                "shouldRefresh: YES (${System.currentTimeMillis()} - $firstRetrievedMillis > $REFRESH_RATE)"
            )
            true
        } else {
            Log.d(TAG, "shouldRefresh: NO (time left: ${(REFRESH_RATE - timeDiff) / 1000}s")
            false
        }
    }


    fun refreshArticles() {
        viewModelScope.launch {
            loadMoreAllowed = false
            if (_status.value!!.isNotLoading()) {
                _status.value = Status.LOADING
                Log.d(TAG, "refreshArticles: Loading start")
                try {
                    appRepository.refreshArticles(category)
                    loadMoreAllowed = true
                    _status.value = Status.SUCCESS
                    _eventResetList.value = true
                } catch (e: Exception) {
                    Log.d(TAG, "refreshArticles: $e")
                    _status.value = Status.FAILURE
                }
                Log.d(TAG, "refreshArticles: Loading finish")
            }
        }
    }

    fun doneResetList() {
        _eventResetList.value = false
    }

    fun addFavoriteArticle(article: Article) {
        viewModelScope.launch {
            appRepository.addFavoriteArticle(article)
        }
    }

    fun removeFavoriteArticle(article: Article) {
        viewModelScope.launch {
            article.favorite = 0
            appRepository.removeFavoriteArticle(article)
        }
    }

}

class ArticleViewModelFactory(
    private val appRepository: AppRepository,
    private val category: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleViewModel(appRepository, category) as T
    }

}