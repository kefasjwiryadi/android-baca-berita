package com.kefasjwiryadi.bacaberita.di

import android.content.Context
import com.kefasjwiryadi.bacaberita.db.AppDatabase
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.network.NewsApiService
import com.kefasjwiryadi.bacaberita.repository.AppRepository
import com.kefasjwiryadi.bacaberita.ui.common.ArticleDetailViewModelFactory
import com.kefasjwiryadi.bacaberita.ui.favorite.FavoriteViewModelFactory
import com.kefasjwiryadi.bacaberita.ui.home.ArticleViewModelFactory
import com.kefasjwiryadi.bacaberita.ui.search.SearchViewModelFactory

/**
 * Manual dependency injection.
 */
object Injection {

    private fun provideDatabase(context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    private fun provideNewsApiService(): NewsApiService {
        return NewsApiService.getService()
    }

    private fun provideRepository(context: Context): AppRepository {
        return AppRepository.getRepository(provideDatabase(context), provideNewsApiService())
    }

    fun provideArticleViewModelFactory(
        context: Context,
        category: String
    ): ArticleViewModelFactory {
        return ArticleViewModelFactory(provideRepository(context), category)
    }

    fun provideSearchViewModelFactory(
        context: Context
    ): SearchViewModelFactory {
        return SearchViewModelFactory(provideRepository(context))
    }

    fun provideFavoriteViewModelFactory(
        context: Context
    ): FavoriteViewModelFactory {
        return FavoriteViewModelFactory(provideRepository(context))
    }

    fun provideArticleDetailViewModelFactory(
        context: Context, article: Article
    ): ArticleDetailViewModelFactory {
        return ArticleDetailViewModelFactory(provideRepository(context), article)
    }
}