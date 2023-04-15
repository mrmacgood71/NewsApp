package it.macgood.newsappapi.ui.di

import it.macgood.data.repository.NewsRepositoryImpl
import it.macgood.data.database.ArticleDatabase
import it.macgood.data.repository.SavedNewsRepositoryImpl
import it.macgood.domain.repository.NewsRepository
import it.macgood.domain.usecase.DeleteSavedNewsUseCase
import it.macgood.domain.usecase.GetSavedNewsUseCase
import it.macgood.domain.usecase.SaveArticleUseCase
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


    single<SaveArticleUseCase> { SaveArticleUseCase(repository = get()) }
    single<GetSavedNewsUseCase> { GetSavedNewsUseCase(repository = get()) }
    single<DeleteSavedNewsUseCase> { DeleteSavedNewsUseCase(repository = get()) }
}