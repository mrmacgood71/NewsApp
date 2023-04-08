package it.macgood.newsappapi.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.FragmentSavedNewsBinding
import it.macgood.newsappapi.ui.NewsAdapter
import it.macgood.newsappapi.ui.NewsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO: 1. save res to db
class SavedNewsFragment : Fragment() {

    private lateinit var binding: FragmentSavedNewsBinding
    private val viewModel: NewsViewModel by viewModel()
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundleOf(
                    Pair("article", it)
                )
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.savedNewsRecyclerView.apply {
            adapter = newsAdapter
        }
    }
}