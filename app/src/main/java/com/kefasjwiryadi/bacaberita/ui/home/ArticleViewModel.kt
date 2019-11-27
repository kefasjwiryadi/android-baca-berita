package com.kefasjwiryadi.bacaberita.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.network.NewsApiService
import com.kefasjwiryadi.bacaberita.repository.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "ArticleViewModel"

const val MINUTE_TO_MILLIS = 60L * 1000L
const val REFRESH_RATE = 10L * MINUTE_TO_MILLIS

class ArticleViewModel(
    private val appRepository: AppRepository,
    private val category: String
) : ViewModel() {

    val articles = appRepository.getArticles(category)

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isLoadingMore = MutableLiveData<Boolean>(false)
    val isLoadingMore: LiveData<Boolean>
        get() = _isLoadingMore

    private var loadMoreAllowed = false

    init {
        Log.d(TAG, "init: $category")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: $category")
    }

    fun onFragmentResume() {
        viewModelScope.launch {
            if (shouldRefresh()) {
                loadMoreAllowed = false
                refreshArticles()
            }
        }
    }

    fun onReachEnd() {
        viewModelScope.launch {
            if (loadMoreAllowed && isLoadingMore.value == false && hasNextPage()) {
                val lastArticle = appRepository.getLastArticle(category) ?: return@launch
                val nextPage = lastArticle.onPage + 1
                Log.d(TAG, "onReachEnd: $nextPage")
                try {
                    _isLoadingMore.value = true
                    appRepository.loadNextPage(category, nextPage)
                } catch (e: Exception) {
                    Log.d(TAG, "onReachEnd: $e")
                }
                _isLoadingMore.value = false
            }
        }
    }

    private suspend fun hasNextPage(): Boolean {
        val lastArticle = appRepository.getLastArticle(category) ?: return false
        return lastArticle.onPage * NewsApiService.PAGE_SIZE_DEF_VALUE < lastArticle.totalResults
    }

    private suspend fun shouldRefresh(): Boolean {
        val firstArticle = appRepository.getFirstArticle(category) ?: return true
        val firstArticleMillis = firstArticle.timeRetrieved
        val timeDiff = System.currentTimeMillis() - firstArticleMillis
        return if (timeDiff > REFRESH_RATE) {
            Log.d(
                TAG,
                "shouldRefresh: YES (${System.currentTimeMillis()} - $firstArticleMillis > $REFRESH_RATE)"
            )
            true
        } else {
            Log.d(TAG, "shouldRefresh: NO (time left: ${(REFRESH_RATE - timeDiff) / 1000}s")
            false
        }
    }


    fun refreshArticles() {
        viewModelScope.launch {
            if (isLoading.value == false) {
                _isLoading.value = true
                Log.d(TAG, "refreshArticles: Loading start")
                try {
                    appRepository.refreshArticles(category)
                    loadMoreAllowed = true
                } catch (e: Exception) {
                    Log.d(TAG, "refreshArticles: $e")
                }
                _isLoading.value = false
                Log.d(TAG, "refreshArticles: Loading finish")
            }
        }
    }

    fun addArticleToFavorite(article: Article) {
        viewModelScope.launch {
            appRepository.addArticleToFavorite(article)
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