package it.macgood.newsappapi.ui.fragment

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.INVISIBLE
    }

    fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

}