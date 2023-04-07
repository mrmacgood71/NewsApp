package it.macgood.newsappapi.repository

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.macgood.newsappapi.database.ArticleDatabase
import it.macgood.newsappapi.ui.NewsViewModel

class NewsViewModelFactory(
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}

fun Activity.factory() =
    NewsViewModelFactory(NewsRepository(ArticleDatabase(this)))

fun Fragment.factory() =
    NewsViewModelFactory(NewsRepository(ArticleDatabase(requireActivity())))