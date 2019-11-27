package com.kefasjwiryadi.bacaberita.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kefasjwiryadi.bacaberita.BuildConfig
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET(TOP_HEADLINES)
    fun getArticles(
        @Query(CATEGORY_KEY) category: String = CATEGORY_DEF_VALUE,
        @Query(API_KEY_KEY) apiKey: String = API_KEY_DEF_VALUE,
        @Query(COUNTRY_KEY) country: String = COUNTRY_DEF_VALUE,
        @Query(PAGE_SIZE_KEY) pageSize: Int = PAGE_SIZE_DEF_VALUE,
        @Query(PAGE_KEY) page: Int = PAGE_DEF_VALUE
    ): Deferred<ArticlesNetworkContainer>

    @GET(EVERYTHING)
    fun searchArticles(
        @Query(QUERY_KEY) query: String = QUERY_DEF_VALUE,
        @Query(API_KEY_KEY) apiKey: String = API_KEY_DEF_VALUE,
        @Query(LANGUAGE_KEY) language: String = LANGUAGE_DEF_VALUE,
        @Query(PAGE_SIZE_KEY) pageSize: Int = PAGE_SIZE_DEF_VALUE,
        @Query(PAGE_KEY) page: Int = PAGE_DEF_VALUE
    ): Deferred<ArticlesNetworkContainer>

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

        private const val QUERY_KEY = "qInTitle"
        private const val QUERY_DEF_VALUE = ""

        private const val COUNTRY_KEY = "country"
        private const val COUNTRY_DEF_VALUE = "id"

        private const val LANGUAGE_KEY = "language"
        private const val LANGUAGE_DEF_VALUE = "id"

        private const val API_KEY_KEY = "apiKey"
        private const val API_KEY_DEF_VALUE = BuildConfig.NEWS_API_KEY

        private const val CATEGORY_KEY = "category"
        private const val CATEGORY_DEF_VALUE = CATEGORY_GENERAL

        private const val PAGE_SIZE_KEY = "pageSize"
        const val PAGE_SIZE_DEF_VALUE = 15

        private const val PAGE_KEY = "page"
        private const val PAGE_DEF_VALUE = 1

        @Volatile
        private var INSTANCE: NewsApiService? = null

        fun getService(): NewsApiService {
            synchronized(this) {
                var localInstance = INSTANCE

                if (localInstance == null) {
                    localInstance = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addCallAdapterFactory(CoroutineCallAdapterFactory())
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