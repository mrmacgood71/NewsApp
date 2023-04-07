package it.macgood.newsappapi.repository

import androidx.room.RoomDatabase
import it.macgood.newsappapi.api.RetrofitInstance
import it.macgood.newsappapi.database.ArticleDatabase

class NewsRepository(
    val database: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

}