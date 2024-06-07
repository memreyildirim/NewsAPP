package com.emreyildirim.newsappkotlin.api

import com.emreyildirim.newsappkotlin.models.NewsResponse
import com.emreyildirim.newsappkotlin.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale.IsoCountryCode

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode: String = "tr",
        @Query("page")
        pageNumber:Int = 1,
        @Query("apiKey")
        apiKey:String =API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber:Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}