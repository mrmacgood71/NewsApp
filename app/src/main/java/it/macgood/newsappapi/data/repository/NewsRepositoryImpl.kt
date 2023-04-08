package it.macgood.newsappapi.data.repository

import it.macgood.newsappapi.domain.api.NewsApi
import it.macgood.newsappapi.data.database.ArticleDatabase
import it.macgood.newsappapi.domain.repository.NewsRepository

class NewsRepositoryImpl(
    val database: ArticleDatabase,
    val api: NewsApi
) : NewsRepository {
    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    override suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        api.searchForNews(searchQuery, pageNumber)
}