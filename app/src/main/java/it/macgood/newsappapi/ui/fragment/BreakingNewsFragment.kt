package it.macgood.newsappapi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import it.macgood.data.model.Article
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.FragmentBreakingNewsBinding
import it.macgood.newsappapi.ui.NewsAdapter
import it.macgood.newsappapi.ui.NewsViewModel
import it.macgood.newsappapi.utils.Resource
import it.macgood.newsappapi.utils.toDataArticle
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO:
//  1. pagination
//  2. design

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
                        newsAdapter.differ.submitList(newsResponse.articles)
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

    private fun setupRecyclerView(): NewsAdapter {
        newsAdapter = NewsAdapter(viewModel)
        binding.breakingNewsRecyclerView.apply {
            adapter = newsAdapter
        }
        return newsAdapter
    }
}