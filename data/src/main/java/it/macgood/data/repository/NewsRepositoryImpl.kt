package it.macgood.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.macgood.data.api.NewsApi
import it.macgood.data.database.ArticleDatabase
import it.macgood.data.database.toArticleDto
import it.macgood.data.database.toDataArticle
import it.macgood.domain.model.Article as ArticleDto
import it.macgood.domain.repository.NewsRepository


class NewsRepositoryImpl(
    val database: ArticleDatabase,
    val api: NewsApi
) : NewsRepository {
    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    override suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        api.searchForNews(searchQuery, pageNumber)
}