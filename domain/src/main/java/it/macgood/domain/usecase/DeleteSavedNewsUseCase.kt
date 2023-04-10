package it.macgood.domain.usecase

import it.macgood.domain.model.Article
import it.macgood.domain.repository.NewsRepository

class DeleteSavedNewsUseCase(
    private val repository: NewsRepository
) {

}