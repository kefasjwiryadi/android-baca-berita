package com.kefasjwiryadi.bacaberita.network

import com.kefasjwiryadi.bacaberita.BuildConfig
import com.kefasjwiryadi.bacaberita.db.TypeConverter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * News REST API access points
 */
interface NewsApiService {

    @GET(TOP_HEADLINES)
    suspend fun getArticles(
        @Query(CATEGORY_KEY) category: String = CATEGORY_DEF_VALUE,
        @Query(API_KEY_KEY) apiKey: String = getApiKey(),
        @Query(COUNTRY_KEY) country: String = COUNTRY_DEF_VALUE,
        @Query(PAGE_SIZE_KEY) pageSize: Int = PAGE_SIZE_DEF_VALUE,
        @Query(PAGE_KEY) page: Int = PAGE_DEF_VALUE
    ): ArticlesNetworkContainer

    @GET(EVERYTHING)
    suspend fun searchArticles(
        @Query(QUERY_KEY) query: String = QUERY_DEF_VALUE,
        @Query(API_KEY_KEY) apiKey: String = getApiKey(),
        @Query(LANGUAGE_KEY) language: String = LANGUAGE_DEF_VALUE,
        @Query(PAGE_SIZE_KEY) pageSize: Int = PAGE_SIZE_DEF_VALUE,
        @Query(PAGE_KEY) page: Int = PAGE_DEF_VALUE
    ): ArticlesNetworkContainer

    @GET
    suspend fun getHtml(@Url url: String): String

    companion object {

        const val CATEGORY_GENERAL = "general"
        const val CATEGORY_BUSINESS = "business"
        const val CATEGORY_ENTERTAINMENT = "entertainment"
        const val CATEGORY_HEALTH = "health"
        const val CATEGORY_SCIENCE = "science"
        const val CATEGORY_SPORTS = "sports"
        const val CATEGORY_TECHNOLOGY = "technology"

        private const val BASE_URL = "https://newsapi.org/v2/"
        private const val TOP_HEADLINES = "top-headlines"
        private const val EVERYTHING = "everything"

        private const val QUERY_KEY = "q"
        private const val QUERY_DEF_VALUE = ""

        private const val COUNTRY_KEY = "country"
        private const val COUNTRY_DEF_VALUE = "id"

        private const val LANGUAGE_KEY = "language"
        private const val LANGUAGE_DEF_VALUE = "id"

        private const val API_KEY_KEY = "apiKey"

        private const val CATEGORY_KEY = "category"
        private const val CATEGORY_DEF_VALUE = CATEGORY_GENERAL

        private const val PAGE_SIZE_KEY = "pageSize"
        const val PAGE_SIZE_DEF_VALUE = 15

        private const val PAGE_KEY = "page"
        private const val PAGE_DEF_VALUE = 1

        private val apiKeys = TypeConverter.stringToStringList(BuildConfig.NEWS_API_KEY)

        private fun getApiKey(): String {
            return apiKeys.random()
        }

        @Volatile
        private var INSTANCE: NewsApiService? = null

        fun getService(): NewsApiService {
            synchronized(this) {
                var localInstance = INSTANCE

                if (localInstance == null) {
                    localInstance = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(NewsApiService::class.java)
                    INSTANCE = localInstance
                }

                return localInstance!!
            }
        }
    }

}