package com.emreyildirim.newsappkotlin.ui.fragments

import android.content.Context
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emreyildirim.newsappkotlin.R
import com.emreyildirim.newsappkotlin.adapters.NewsAdapter
import com.emreyildirim.newsappkotlin.databinding.FragmentNewsBinding
import com.emreyildirim.newsappkotlin.ui.NewsActivity
import com.emreyildirim.newsappkotlin.ui.NewsViewModel
import com.emreyildirim.newsappkotlin.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.emreyildirim.newsappkotlin.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsFragment : Fragment(R.layout.fragment_news) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemNewsError:CardView
    lateinit var binding: FragmentNewsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)


        itemNewsError = view.findViewById(R.id.itemNewsError)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = inflater.inflate(R.layout.item_error,null)

        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupNewsRecycler()

        newsAdapter.setOnItemClickListener { article ->
            article?.let {
                val bundle = Bundle().apply {
                    putSerializable("article", it)
                }
                findNavController().navigate(R.id.action_newsFragment_to_articleFragment, bundle)
            }
        }


        val SEARCH_NEWS_TIME_DELAY = 500L
        var job: Job? = null
        binding.searchEdit.addTextChangedListener(){editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        newsViewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / com.emreyildirim.newsappkotlin.util.Constants.QUERY_PAGE_SIZE + 2
                        isLastPage= newsViewModel.searchNewsPage == totalPages
                        if (isLastPage){
                            binding.recyclerSearch.setPadding(0,0,0,0)
                        }
                    }

                }
                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"Sorry error: $message", Toast.LENGTH_LONG)
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading<*> -> {
                    showProgressBar()
                }

            }
        })

        retryButton.setOnClickListener {
            if (binding.searchEdit.text.toString().isNotEmpty()){
                newsViewModel.searchNews(binding.searchEdit.text.toString())
            } else{
                hideErrorMessage()
            }
        }


    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar(){

        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
     private fun showProgressBar(){
         binding.paginationProgressBar.visibility = View.VISIBLE
         isLoading = true
     }
    private fun hideErrorMessage(){
        itemNewsError.visibility = View.INVISIBLE
        isError=false
    }
    private fun showErrorMessage(message : String){
        itemNewsError.visibility=View.VISIBLE
        errorText.text=message
        isError=true
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVissible =
                totalItemCount >= com.emreyildirim.newsappkotlin.util.Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isTotalMoreThanVissible && isScrolling
            if (shouldPaginate) {
                newsViewModel.searchNews(binding.searchEdit.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

        private fun setupNewsRecycler(){
            newsAdapter = NewsAdapter()
            binding.recyclerSearch.apply {
                adapter =newsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(this@NewsFragment.scrollListener)
            }
        }
    }




