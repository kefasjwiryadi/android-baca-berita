package com.kefasjwiryadi.bacaberita.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kefasjwiryadi.bacaberita.db.AppDatabase
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.network.NewsApiService
import com.kefasjwiryadi.bacaberita.util.cleanContent
import com.kefasjwiryadi.bacaberita.util.cleanTitle
import com.kefasjwiryadi.bacaberita.util.clearUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "AppRepository"

class AppRepository(
    private val appDatabase: AppDatabase,
    private val newsApiService: NewsApiService
) {


    private val articleDao = appDatabase.articleDao()

    fun getArticles(category: String): LiveData<List<Article>> {
        return articleDao.getArticles(category)
    }

    suspend fun getFirstArticle(category: String): Article? = withContext(Dispatchers.IO) {
        return@withContext articleDao.getFirstArticle(category)
    }

    suspend fun getLastArticle(category: String): Article? = withContext(Dispatchers.IO) {
        return@withContext articleDao.getLastArticle(category)
    }

    suspend fun refreshArticles(category: String) {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "refreshArticles: getting articles $category from network...")
            val articlesContainer =
                newsApiService.getArticles(category = category, page = 1).await()
            val articles = articlesContainer.articles
            articles.fillArticleData(category, articlesContainer.totalResults, 1)
            Log.d(
                TAG,
                "refreshArticles: articles from network: \n\t${articles[0]}\n\t${articles[1]}"
            )
            Log.d(TAG, "refreshArticles: clearing database...")
            articleDao.clear(category)
            Log.d(TAG, "refreshArticles: inserting articles to database...")
            articleDao.insertArticles(articles)
            Log.d(TAG, "refreshArticles: done")
        }
    }

    suspend fun loadNextPage(category: String, page: Int) {
        withContext(Dispatchers.IO) {
            val articlesContainer =
                newsApiService.getArticles(category = category, page = page).await()
            val articles = articlesContainer.articles
            articles.fillArticleData(category, articlesContainer.totalResults, page)
            articleDao.insertArticles(articles)
        }
    }

    private fun List<Article>.fillArticleData(category: String, totalResults: Int, page: Int) {
        this.forEach {
            it.category = category
            it.timeRetrieved = System.currentTimeMillis()
            it.totalResults = totalResults
            it.onPage = page
            it.url += "<$category"
            it.content = it.content?.cleanContent()
            it.title = it.title?.cleanTitle()
        }
    }

    suspend fun searchArticles(query: String, page: Int) = withContext(Dispatchers.IO) {
        val articlesNetworkContainer =
            newsApiService.searchArticles(query = query, page = page).await()
        val articles = articlesNetworkContainer.articles
        articles.fillArticleData("search", articlesNetworkContainer.totalResults, page)
        return@withContext articles
    }

    fun getFavorites(): LiveData<List<Article>> {
        return articleDao.getArticlesReversed("favorite")
    }

    fun isArticleFavorited(article: Article): LiveData<Boolean> {
        val favoriteArticle = articleDao.getArticleLiveData(article.url.clearUrl() + "<favorite")
        Log.d(TAG, "isArticleFavorited: ${favoriteArticle.value}")
        return Transformations.map(favoriteArticle) {
            it != null
        }
    }

    suspend fun addArticleToFavorite(article: Article) {
        withContext(Dispatchers.IO) {
            val favoriteArticle = article.copy()
            favoriteArticle.category = "favorite"
            favoriteArticle.url = favoriteArticle.url.clearUrl() + "<favorite"
            favoriteArticle.timeRetrieved = System.currentTimeMillis()
            articleDao.addArticleToFavorite(favoriteArticle)
        }
    }

    suspend fun deleteArticleFromFavorite(article: Article) {
        withContext(Dispatchers.IO) {
            val article = articleDao.getArticle(article.url.clearUrl() + "<favorite")
            if (article != null) {
                articleDao.deleteArticleFromFavorite(article)
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: AppRepository? = null

        fun getRepository(
            appDatabase: AppDatabase,
            newsApiService: NewsApiService
        ): AppRepository {
            synchronized(this) {
                var localInstance = INSTANCE
                if (localInstance == null) {
                    localInstance = AppRepository(appDatabase, newsApiService)
                    INSTANCE = localInstance
                }
                return localInstance
            }
        }

    }

}