package it.macgood.newsappapi.ui.di

import it.macgood.data.repository.NewsRepositoryImpl
import it.macgood.data.database.ArticleDatabase
import it.macgood.data.repository.SavedNewsRepositoryImpl
import it.macgood.domain.repository.NewsRepository
import org.koin.dsl.module

val dataModule = module {

    single<ArticleDatabase> {
        ArticleDatabase(context = get())
    }

    single<NewsRepository> {
        NewsRepositoryImpl(database = get(), api = get())
    }

    single {
        SavedNewsRepositoryImpl(database = get())
    }
}