package it.macgood.newsappapi.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AbsListView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import it.macgood.data.api.Constants.Companion.QUERY_PAGE_SIZE
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.FragmentBreakingNewsBinding
import it.macgood.newsappapi.ui.fragment.adapter.NewsAdapter
import it.macgood.newsappapi.ui.fragment.adapter.StoryCircleAdapter
import it.macgood.newsappapi.ui.fragment.viewmodel.NewsViewModel
import it.macgood.newsappapi.utils.Resource
import it.macgood.newsappapi.utils.TimeChecker
import it.macgood.newsappapi.utils.TimeOfDay
import it.macgood.newsappapi.utils.toDataArticle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        configureAppBar()

        newsAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundleOf(
                    Pair("article", it.toDataArticle())
                )
            )
        }

        val storyCircleAdapter = StoryCircleAdapter(images)


        viewModel.getNewsBySource("techcrunch,cnn,reuters,bbc-news,abc-news,bloomberg,espn,the-washington-post")

        viewModel.sourceNews.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Success -> {
                    response.data?.let {
                        storyCircleAdapter.differ.submitList(it.articles)


                        binding.circleStoriesRecyclerView.adapter = storyCircleAdapter
                        storyCircleAdapter.differ.currentList.forEach { Log.d(TAG, "onCreateView: $it") }

                    }
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    response.message?.let {
                        Log.d(TAG, "onCreateViewError: $it")
                    }
                }
            }

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
                        Log.d(TAG, "onCreateViewError: $it")
                    }
                }
                is Resource.Loading -> {
                }
            }
        }

        return binding.root
    }

    private fun configureAppBar() {
        val window = requireActivity().window
        val timeOfDay = TimeChecker.getTimeOfDay()
        val toolbar = binding.appbar.getChildAt(0) as CollapsingToolbarLayout
        val username = "user"

        if (timeOfDay == TimeOfDay.DAY) {
            window.statusBarColor = getColor(R.color.lightOrangeLogo)

            toolbar.contentScrim = getDrawable(R.color.lightOrangeLogo)
            toolbar.title = "Good day, $username"
//            toolbar.setExpandedTitleColor(getColor(R.color.dayTitleColor))

            binding.appbar.background = getDrawable(R.drawable.sunrise)
        } else if (timeOfDay == TimeOfDay.EVENING) {
            window.statusBarColor = getColor(R.color.eveningColor)

            toolbar.contentScrim = getDrawable(R.color.eveningColor)
            toolbar.title = "Good evening, $username"

            binding.appbar.background = getDrawable(R.drawable.sunset11)
        } else {
            window.statusBarColor = getColor(R.color.nightColor)

            toolbar.contentScrim = getDrawable(R.color.nightColor)
            toolbar.title = "Good night, $username"

            binding.appbar.background = getDrawable(R.drawable.night1)
        }
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
        newsAdapter = NewsAdapter()
        binding.breakingNewsRecyclerView.apply {
            adapter = newsAdapter
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
        return newsAdapter
    }

    companion object {
        const val TAG = "TAG"
        val images: List<Int> = listOf(
            R.drawable.first,
            R.drawable.second,
            R.drawable.third,
            R.drawable.first,
            R.drawable.second,
            R.drawable.third,
            R.drawable.second,
            R.drawable.third,
        )
    }

}
