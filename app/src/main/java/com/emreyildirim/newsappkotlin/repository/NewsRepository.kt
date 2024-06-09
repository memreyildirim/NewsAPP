package com.emreyildirim.newsappkotlin.repository

import android.util.Log
import com.emreyildirim.newsappkotlin.api.RetrofitInstance
import com.emreyildirim.newsappkotlin.db.ArticleDatabase
import com.emreyildirim.newsappkotlin.models.Article
import retrofit2.http.Query
import java.util.Locale.IsoCountryCode

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getHeadlines(countryCode: String, pageNumber: Int)=
        RetrofitInstance.api.getHeadlines()

    suspend fun searchNews(searchQuery: String, pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article) {
        val existingArticle = db.getArticleDao().getArticleByUrl(article.url)
        if (existingArticle == null) {
            val maxId = db.getArticleDao().getMaxArticleId() ?: 0
            article.id = maxId + 1
            db.getArticleDao().upsert(article)
            Log.d("NewsRepository", "Article added to favorites: ${article.id} ${article.title}")//This is for  control added news

        }
    }


    fun getFavoriteNews() = db.getArticleDao().getAllArticles()


    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticles(article)


}