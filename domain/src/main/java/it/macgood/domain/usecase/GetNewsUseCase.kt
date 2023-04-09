package it.macgood.domain.usecase

import it.macgood.domain.model.NewsResponse
import it.macgood.domain.repository.NewsRepository
import retrofit2.Response

class GetNewsUseCase(
    private val repository: NewsRepository
) {
    suspend fun execute(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return repository.getBreakingNews(countryCode, pageNumber)
    }
}