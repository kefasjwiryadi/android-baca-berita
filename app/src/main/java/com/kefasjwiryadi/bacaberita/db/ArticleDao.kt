package com.kefasjwiryadi.bacaberita.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kefasjwiryadi.bacaberita.domain.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article_table WHERE category = :category ORDER BY timeRetrieved ASC")
    fun getArticles(category: String): LiveData<List<Article>>

    @Query("SELECT * FROM article_table WHERE category = :category ORDER BY timeRetrieved DESC")
    fun getArticlesReversed(category: String): LiveData<List<Article>>

    @Query("SELECT * FROM article_table WHERE url= :url LIMIT 1")
    fun getArticleLiveData(url: String): LiveData<Article>

    @Query("SELECT * FROM article_table WHERE url = :url")
    suspend fun getArticle(url: String): Article

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticleToFavorite(article: Article)

    @Delete
    suspend fun deleteArticleFromFavorite(article: Article)

    @Query("DELETE FROM article_table WHERE category = :category")
    suspend fun clear(category: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticles(articles: List<Article>)

    @Query("SELECT * FROM article_table WHERE category = :category ORDER BY timeRetrieved ASC LIMIT 1 ")
    suspend fun getFirstArticle(category: String): Article

    @Query("SELECT * FROM article_table WHERE category = :category ORDER BY timeRetrieved DESC LIMIT 1")
    suspend fun getLastArticle(category: String): Article

}