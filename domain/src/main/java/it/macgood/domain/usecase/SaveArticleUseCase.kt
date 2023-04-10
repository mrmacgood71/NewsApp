package it.macgood.domain.usecase

import it.macgood.domain.model.Article
import it.macgood.domain.repository.NewsRepository

class SaveArticleUseCase(
    private val repository: NewsRepository
) {
}