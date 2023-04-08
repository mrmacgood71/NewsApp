package it.macgood.newsappapi.domain.repository

import it.macgood.newsappapi.domain.model.NewsResponse
import retrofit2.Response

interface NewsRepository {

    suspend fun getBreakingNews(countyCode: String, pageNumber: Int) : Response<NewsResponse>

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>

}