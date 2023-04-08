package it.macgood.newsappapi.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.macgood.newsappapi.domain.model.Source
import java.io.Serializable

// TODO: 1. fix
@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
) : Serializable