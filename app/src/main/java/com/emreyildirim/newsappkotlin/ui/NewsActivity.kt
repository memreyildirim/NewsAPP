package com.emreyildirim.newsappkotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.emreyildirim.newsappkotlin.R
import com.emreyildirim.newsappkotlin.databinding.ActivityNewsBinding
import com.emreyildirim.newsappkotlin.db.ArticleDatabase
import com.emreyildirim.newsappkotlin.repository.NewsRepository

class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProivderFactory = NewsViewModelProivderFactory(application,newsRepository)
        newsViewModel = ViewModelProvider(this,viewModelProivderFactory).get(NewsViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController =navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}