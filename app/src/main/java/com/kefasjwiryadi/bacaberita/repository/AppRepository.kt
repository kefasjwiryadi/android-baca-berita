package com.kefasjwiryadi.bacaberita.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kefasjwiryadi.bacaberita.db.AppDatabase
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.domain.ArticleFetchResult
import com.kefasjwiryadi.bacaberita.domain.ArticleSearchResult
import com.kefasjwiryadi.bacaberita.network.NewsApiService
import com.kefasjwiryadi.bacaberita.util.AbsentLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "AppRepository"

class AppRepository(
    private val appDatabase: AppDatabase,
    private val newsApiService: NewsApiService
) {


    private val articleDao = appDatabase.articleDao()

    fun getArticles(category: String): LiveData<List<Article>> {
        return Transformations.switchMap(articleDao.getArticles(category)) { articleFetchResult ->
            if (articleFetchResult != null) {
                articleDao.getArticlesByUrlsOrdered(articleFetchResult.articleUrls)
            } else {
                AbsentLiveData.create()
            }
        }
    }

    fun getFavoriteArticles(): LiveData<List<Article>> {
        return articleDao.getFavoriteArticles()
    }

    suspend fun getArticleFetchResult(category: String): ArticleFetchResult? =
        withContext(Dispatchers.IO) {
            return@withContext articleDao.getArticleFetchResult(category)
        }

    suspend fun refreshArticles(category: String) {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "refreshArticles: getting articles $category from network...")
            val articlesContainer =
                newsApiService.getArticles(category = category, page = 1)
            val articles = articlesContainer.articles
            Log.d(
                TAG,
                "refreshArticles: articles from network: \n\t${articles[0]}\n\t${articles[1]}"
            )
            Log.d(TAG, "refreshArticles: clearing database...")

            Log.d(TAG, "refreshArticles: inserting articles to database...")
            articleDao.insertArticles(articles)
            articleDao.insertArticleFetchResult(
                ArticleFetchResult(category, articles.map {
                    it.url
                }, articlesContainer.totalResults, 1)
            )
            Log.d(TAG, "refreshArticles: done")
        }
    }

    suspend fun loadNextPage(category: String, page: Int) {
        Log.d(TAG, "loadNextPage: $category $page")
        withContext(Dispatchers.IO) {
            val articlesContainer =
                newsApiService.getArticles(category = category, page = page)
            val articles = articlesContainer.articles
            articleDao.insertArticles(articles)

            // Merge result with previous result
            val previousResult = articleDao.getArticleFetchResult(category)
            if (previousResult != null) {
                val urls = ArrayList(previousResult.articleUrls)
                urls.addAll(articlesContainer.articles.map { article ->
                    article.url
                })
                articleDao.insertArticleFetchResult(
                    ArticleFetchResult(category, urls, articlesContainer.totalResults, page)
                )
            }
        }
    }

    suspend fun addFavoriteArticle(article: Article) {
        withContext(Dispatchers.IO) {
            article.favorite = System.currentTimeMillis()
            articleDao.insertArticle(article)
        }
    }

    suspend fun removeFavoriteArticle(article: Article) {
        withContext(Dispatchers.IO) {
            article.favorite = 0
            articleDao.insertArticle(article)
        }
    }

    suspend fun insertArticle(article: Article) {
        withContext(Dispatchers.IO) {
            articleDao.insertArticle(article)
        }
    }

    suspend fun searchArticles(query: String, page: Int) = withContext(Dispatchers.IO) {
        val articlesNetworkContainer =
            newsApiService.searchArticles(query = query, page = page)
        val articles = articlesNetworkContainer.articles
        return@withContext ArticleSearchResult(
            query,
            articles,
            articlesNetworkContainer.totalResults,
            page
        )
    }

    fun getArticleLd(url: String): LiveData<Article> = articleDao.getArticleLd(url)

    suspend fun getArticle(url: String): Article? = withContext(Dispatchers.IO) {
        return@withContext articleDao.getArticle(url)
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