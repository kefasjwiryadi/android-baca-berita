package com.kefasjwiryadi.bacaberita.ui.search

import android.util.Log
import androidx.lifecycle.*
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.network.NewsApiService
import com.kefasjwiryadi.bacaberita.repository.AppRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel(private val appRepository: AppRepository) : ViewModel() {

    private val _articles = MutableLiveData<ArrayList<Article>>()
    val articles: LiveData<ArrayList<Article>>
        get() = _articles

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isLoadingMore = MutableLiveData<Boolean>(false)
    val isLoadingMore: LiveData<Boolean>
        get() = _isLoadingMore

    private lateinit var query: String

    fun onReachEnd() {
        val nextPage = getNextPage()
        if (nextPage != -1) {
            loadNextPage(query, nextPage)
        }
    }

    fun getNextPage(): Int {
        val lastArticle = _articles.value?.last() ?: return -1
        if (lastArticle.onPage * NewsApiService.PAGE_SIZE_DEF_VALUE < lastArticle.totalResults) {
            return lastArticle.onPage + 1
        } else {
            return -1
        }
    }

    private fun loadNextPage(query: String, page: Int) {
        viewModelScope.launch {
            if (_isLoadingMore.value == false) {
                _isLoadingMore.value = true
                try {
                    val articlesTemp = appRepository.searchArticles(query, page)
                    if (articlesTemp.isNotEmpty()) {
                        _articles.value = ArrayList(_articles.value)
                        _articles.value?.addAll(articlesTemp)
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onReachEnd: $e")
                }
                _isLoadingMore.value = false
            }
        }
    }

    fun searchArticles(query: String) {
        this.query = query
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d(TAG, "searchArticles: Searching query: $query")
                _articles.value = ArrayList(appRepository.searchArticles(query, 1))
                Log.d(TAG, "searchArticles: Search finished: $query ${(_articles.value)?.get(0)}")
            } catch (e: Exception) {
                Log.d(TAG, "searchArticles: $e")
            }
            _isLoading.value = false
        }
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}

class SearchViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(appRepository) as T
    }

}