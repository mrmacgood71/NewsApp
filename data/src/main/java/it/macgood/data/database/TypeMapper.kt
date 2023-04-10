package it.macgood.data.database

import it.macgood.data.model.Article as DataArticle
import it.macgood.domain.model.Article as ArticleDto
import it.macgood.data.model.Source as DataSource
import it.macgood.domain.model.Source as DomainSource

fun ArticleDto.toDataArticle() : DataArticle {
    return DataArticle(
        this.id,
        this.author,
        this.content,
        this.description,
        this.publishedAt,
        DataSource(
            //api may response null field, but it is never used
            this.source.id.let { "0" },
            this.source.name
        ),
        this.title,
        this.url,
        this.urlToImage
    )
}

fun DataArticle.toArticleDto() : ArticleDto {
    return ArticleDto(
        this.id,
        this.author,
        this.content,
        this.description,
        this.publishedAt,
        DomainSource(this.source.id.let { "0" }, this.source.name),
        this.title,
        this.url,
        this.urlToImage
    )
}