package it.macgood.newsappapi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.macgood.data.api.Constants.Companion.QUERY_PAGE_SIZE
import it.macgood.data.model.Article
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.FragmentBreakingNewsBinding
import it.macgood.newsappapi.ui.NewsAdapter
import it.macgood.newsappapi.ui.NewsViewModel
import it.macgood.newsappapi.utils.Resource
import it.macgood.newsappapi.utils.toDataArticle
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.RecursiveAction

class BreakingNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentBreakingNewsBinding
    private val viewModel: NewsViewModel by viewModel()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundleOf(
                    Pair("article", it.toDataArticle())
                )
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage) {
                            binding.breakingNewsRecyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Log.d("TAG", "onCreateViewError: $it")
                    }
                }
                is Resource.Loading -> {
                }
            }
        }

        return binding.root
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView(): NewsAdapter {
        newsAdapter = NewsAdapter(viewModel)
        binding.breakingNewsRecyclerView.apply {
            adapter = newsAdapter
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
        return newsAdapter
    }
}