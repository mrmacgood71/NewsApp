package it.macgood.domain.usecase

import it.macgood.domain.repository.NewsRepository

class GetSavedNewsUseCase(
    private val repository: NewsRepository
) {

}