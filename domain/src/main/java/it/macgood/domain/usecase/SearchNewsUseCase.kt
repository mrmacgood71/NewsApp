package it.macgood.domain.usecase

import it.macgood.domain.model.NewsResponse
import it.macgood.domain.repository.NewsRepository
import retrofit2.Response

class SearchNewsUseCase(
    private val repository: NewsRepository
) {

    suspend fun execute(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
        return repository.searchNews(searchQuery, pageNumber)
    }
}