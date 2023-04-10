package it.macgood.newsappapi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import it.macgood.data.database.toArticleDto
import it.macgood.newsappapi.databinding.FragmentArticleBinding
import it.macgood.data.model.Article
import it.macgood.newsappapi.ui.NewsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO:
//  1. fix webclient
//  2. mb use vm instead of bundle
class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private val viewModel: NewsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)

        val article = arguments?.getSerializable("article") as Article
        Log.d("TAG", "onCreateView: ${article.url}")

        binding.webView.webViewClient = WebViewClient()
        if (article.url != null) {
            binding.webView.loadUrl(article.url)
        }

        binding.fab.setOnClickListener { view ->
            viewModel.saveArticle(article.toArticleDto())
            Snackbar.make(view, "Article is saved", Snackbar.LENGTH_SHORT).show()
        }
        return binding.root
    }
}