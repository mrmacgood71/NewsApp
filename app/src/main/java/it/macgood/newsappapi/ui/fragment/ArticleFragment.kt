package it.macgood.newsappapi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import it.macgood.newsappapi.databinding.FragmentArticleBinding
import it.macgood.data.model.Article

// TODO:
//  1. fix webclient
//  2. mb use vm instead of bundle
class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)

        val article = arguments?.getSerializable("article") as it.macgood.data.model.Article
        Log.d("TAG", "onCreateView: ${article.url}")

        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(article.url)


        return binding.root
    }
}