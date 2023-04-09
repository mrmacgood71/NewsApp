package it.macgood.domain.repository

import it.macgood.domain.model.NewsResponse
import retrofit2.Response

interface NewsRepository {

    suspend fun getBreakingNews(countyCode: String, pageNumber: Int) : Response<NewsResponse>

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>

}