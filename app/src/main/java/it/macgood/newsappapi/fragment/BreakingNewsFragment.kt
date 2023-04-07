package it.macgood.newsappapi.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.FragmentBreakingNewsBinding
import it.macgood.newsappapi.repository.factory
import it.macgood.newsappapi.ui.NewsAdapter
import it.macgood.newsappapi.ui.NewsViewModel
import it.macgood.newsappapi.utils.Resource

// TODO:
//  1. pagination
//  2. di for viewmodel
//  3. design

class BreakingNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentBreakingNewsBinding
    val viewModel: NewsViewModel by viewModels{ factory() }
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        Log.d("TAG", "onCreateView: ")
        newsAdapter.setOnItemClickListener {
            Log.d("TAG", "onCreateView: $it")
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundleOf(
                    Pair("article", it)
                )
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
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

    private fun setupRecyclerView(): NewsAdapter {
        newsAdapter = NewsAdapter()
        binding.breakingNewsRecyclerView.apply {
            adapter = newsAdapter
        }
        return newsAdapter
    }
}