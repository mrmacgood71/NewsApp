package it.macgood.newsappapi.model

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)