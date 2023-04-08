package it.macgood.newsappapi.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import it.macgood.newsappapi.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

}