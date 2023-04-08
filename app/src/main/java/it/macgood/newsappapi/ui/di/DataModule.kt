package it.macgood.newsappapi.ui.di

import it.macgood.newsappapi.data.repository.NewsRepositoryImpl
import it.macgood.newsappapi.data.database.ArticleDatabase
import org.koin.dsl.module

val dataModule = module {

    single<ArticleDatabase> {
        ArticleDatabase(context = get())
    }

    single<NewsRepositoryImpl> {
        NewsRepositoryImpl(database = get(), api = get())
    }
}