package it.macgood.domain.usecase

import it.macgood.domain.model.NewsResponse
import it.macgood.domain.repository.NewsRepository
import retrofit2.Response

class GetNewsBySourceUseCase(
    private val repository: NewsRepository
) {
    suspend fun execute(source: String): Response<NewsResponse> {
        return repository.getBreakingNewsBySources(source)
    }
}