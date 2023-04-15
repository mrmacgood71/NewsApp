package it.macgood.newsappapi.ui.fragment

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.INVISIBLE
    }

    fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }


    fun Fragment.getDrawable(id: Int) : Drawable? {
        return ResourcesCompat.getDrawable(resources, id, requireActivity().theme)
    }

    fun Fragment.getColor(id: Int): Int {
        return resources.getColor(id, requireActivity().theme)
    }

}
