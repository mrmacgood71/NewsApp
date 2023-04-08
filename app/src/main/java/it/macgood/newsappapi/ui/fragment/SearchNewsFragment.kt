package it.macgood.newsappapi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.FragmentSearchNewsBinding
import it.macgood.newsappapi.ui.NewsAdapter
import it.macgood.newsappapi.ui.NewsViewModel
import it.macgood.newsappapi.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO:
//  1. pagination
//  2. design
//  3. copy to main fragment as a button

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
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        newsAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundleOf(
                    Pair("article", it)
                )
            )
        }

        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar(binding.paginationProgressBar)

                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
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

        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.searchNewsRecyclerView.apply {
            adapter = newsAdapter
        }
    }
}