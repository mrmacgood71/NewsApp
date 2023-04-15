package it.macgood.newsappapi.ui.di

import it.macgood.newsappapi.ui.fragment.viewmodel.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<NewsViewModel> {
        NewsViewModel(
            getNewsUseCase = get(),
            searchNewsUseCase = get(),
            getNewsBySourceUseCase = get(),
            savedNewsRepositoryImpl = get()
        )
    }
}