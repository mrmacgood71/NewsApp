package it.macgood.data.repository

import it.macgood.data.api.NewsApi
import it.macgood.data.database.ArticleDatabase
import it.macgood.domain.model.NewsResponse
import it.macgood.domain.repository.NewsRepository
import retrofit2.Response


class NewsRepositoryImpl(
    val database: ArticleDatabase,
    val api: NewsApi
) : NewsRepository {
    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    override suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        api.searchForNews(searchQuery, pageNumber)

    override suspend fun getBreakingNewsBySources(source: String): Response<NewsResponse> =
        api.getBreakingNewsBySources(source)
}