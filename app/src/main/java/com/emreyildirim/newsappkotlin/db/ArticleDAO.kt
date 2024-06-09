package com.emreyildirim.newsappkotlin.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emreyildirim.newsappkotlin.models.Article

@Dao
interface ArticleDAO {

    @Query("SELECT MAX(id) FROM articles")
    suspend fun getMaxArticleId():Int?

    @Query("SELECT * FROM articles WHERE url= :url LIMIT 1")
    suspend fun getArticleByUrl(url: String):Article?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long

    @Query("SELECT * FROM articles")
    fun getAllArticles():LiveData<List<Article>>

    @Delete()
    suspend fun deleteArticles(article: Article)
}