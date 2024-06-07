package com.emreyildirim.newsappkotlin.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emreyildirim.newsappkotlin.repository.NewsRepository

class NewsViewModelProivderFactory (val app:Application , val newsRepository:NewsRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepository)as T
    }
}