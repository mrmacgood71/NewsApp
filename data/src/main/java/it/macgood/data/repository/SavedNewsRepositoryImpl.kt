package it.macgood.data.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import it.macgood.data.database.ArticleDatabase
import it.macgood.data.model.Article

class SavedNewsRepositoryImpl(
    private val database: ArticleDatabase
) {

    suspend fun upsert(article: Article): Long = database.getArticleDao().upsert(article)

    fun getAllArticles(): LiveData<List<Article>> = database.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = database.getArticleDao().deleteArticle(article)

}