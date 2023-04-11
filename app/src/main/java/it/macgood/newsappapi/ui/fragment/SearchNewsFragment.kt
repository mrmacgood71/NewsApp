package it.macgood.newsappapi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.macgood.data.api.Constants
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.FragmentSearchNewsBinding
import it.macgood.newsappapi.ui.NewsAdapter
import it.macgood.newsappapi.ui.NewsViewModel
import it.macgood.newsappapi.utils.Resource
import it.macgood.newsappapi.utils.toDataArticle
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


// TODO:
//  1. copy to main fragment as a button
const val INPUT_TYPE_DELAY = 500L

class SearchNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchNewsBinding
    private val viewModel: NewsViewModel by viewModel()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        setupRecyclerView()

        var job: Job? = null

        binding.searchEditText.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {

                delay(INPUT_TYPE_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        newsAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundleOf(
                    Pair("article", it.toDataArticle())
                )
            )
        }

        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar(binding.paginationProgressBar)

                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if (isLastPage) {
                            binding.searchNewsRecyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar(binding.paginationProgressBar)
                    response.message?.let {
                        Log.d("TAG", "onCreateView: $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar(binding.paginationProgressBar)
                }
            }
        }

        binding.searchImageButton.setOnClickListener {
            binding.searchImageButton.visibility = View.GONE

            val imm: InputMethodManager? =
                getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)
            binding.searchEditText.requestFocus()
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
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.searchNews(binding.searchEditText.toString())
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(viewModel)
        binding.searchNewsRecyclerView.apply {
            adapter = newsAdapter
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }
}