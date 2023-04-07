package it.macgood.newsappapi.fragment

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import it.macgood.newsappapi.ui.NewsAdapter

open class BaseFragment : Fragment() {

    fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.INVISIBLE
    }

    fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

}