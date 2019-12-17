package com.kefasjwiryadi.bacaberita.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.domain.ArticleFetchResult
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

/**
 * Data Access Object for [Article] and [ArticleFetchResult].
 */
@Dao
abstract class ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertArticles(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertArticleFetchResult(articleFetchResult: ArticleFetchResult)

    @Query("SELECT * FROM article_table WHERE url in (:urls)")
    abstract fun getArticlesByUrls(urls: List<String>): LiveData<List<Article>>

    fun getArticlesByUrlsOrdered(urls: List<String>): LiveData<List<Article>> {
        val originalPosition = HashMap<String, Int>()
        urls.forEachIndexed { index, url ->
            originalPosition[url] = index
        }
        Timber.d("getArticlesByUrlsOrdered: ${urls.size} $urls")
        return Transformations.map(getArticlesByUrls(urls)) { articles ->
            Collections.sort(articles) { article1, article2 ->
                val pos1 = originalPosition[article1.url]
                val pos2 = originalPosition[article2.url]
                (pos1 ?: 0).compareTo(pos2 ?: 0)
            }
            articles
        }
    }

    @Query("SELECT * FROM ArticleFetchResult WHERE category = :category")
    abstract fun getArticles(category: String): LiveData<ArticleFetchResult>

    @Query("SELECT * FROM ArticleFetchResult WHERE category = :category")
    abstract suspend fun getArticleFetchResult(category: String): ArticleFetchResult?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertArticle(article: Article)

    @Query("SELECT * FROM article_table WHERE favorite > 0 ORDER BY favorite DESC")
    abstract fun getFavoriteArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM article_table WHERE url = :url")
    abstract fun getArticleLd(url: String): LiveData<Article>

    @Query("SELECT * FROM article_table WHERE url = :url")
    abstract suspend fun getArticle(url: String): Article?
}