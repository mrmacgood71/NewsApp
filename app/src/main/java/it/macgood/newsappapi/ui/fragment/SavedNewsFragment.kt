package it.macgood.newsappapi.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import it.macgood.data.database.toArticleDto
import it.macgood.domain.model.Article
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.FragmentSavedNewsBinding
import it.macgood.newsappapi.ui.NewsAdapter
import it.macgood.newsappapi.ui.NewsViewModel
import it.macgood.newsappapi.utils.toDataArticle
import org.koin.androidx.viewmodel.ext.android.viewModel

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
                    Pair("article", it.toDataArticle())
                )
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(viewHolder.itemView, "Deleted", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.savedNewsRecyclerView)
        }
        // TODO: fix duplicates
        viewModel.getSavedNews().observe(viewLifecycleOwner) { articles ->
            val list: MutableList<Article> = mutableListOf()
            articles.forEach { list.add(it.toArticleDto()) }
            newsAdapter.differ.submitList(list)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(viewModel)
        binding.savedNewsRecyclerView.apply {
            adapter = newsAdapter
        }
    }
}