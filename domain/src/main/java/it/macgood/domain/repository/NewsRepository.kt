package it.macgood.domain.repository

import it.macgood.domain.model.NewsResponse
import retrofit2.Response

interface NewsRepository {

    suspend fun getBreakingNews(countyCode: String, pageNumber: Int) : Response<NewsResponse>

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>
// TODO:
//  1. implement this

//    suspend fun upsert(articleDto: Article): Long
//
//    fun getAllArticles(): LiveData<List<Article>>
//
//    suspend fun deleteArticle(articleDto: Article)

}