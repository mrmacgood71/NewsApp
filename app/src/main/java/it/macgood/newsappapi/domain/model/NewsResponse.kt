package it.macgood.newsappapi.domain.model

import it.macgood.newsappapi.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)