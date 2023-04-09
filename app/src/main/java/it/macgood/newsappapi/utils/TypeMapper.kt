package it.macgood.newsappapi.utils

import it.macgood.data.model.Article as DataArticle
import it.macgood.domain.model.Article as DomainArticle
import it.macgood.data.model.Source

fun DomainArticle.toDataArticle() : DataArticle {
    return DataArticle(
        this.id,
        this.author,
        this.content,
        this.description,
        this.publishedAt,
        Source(
            //api may response null field, but it is never used
            this.source.id.let { "0" },
            this.source.name
        ),
        this.title,
        this.url,
        this.urlToImage
    )
}