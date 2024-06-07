package com.emreyildirim.newsappkotlin.repository

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

    suspend fun upsert(article: Article)=db.getArticleDao().upsert(article)

    fun getFavoriteNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticles(article)

}