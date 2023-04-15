package it.macgood.newsappapi.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.FragmentStoryBinding
import it.macgood.newsappapi.databinding.ItemStoryCircleBinding


class StoryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentStoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentStoryBinding.inflate(inflater, container, false)

        val imageUrl = arguments?.getString("imageUrl")
        val description = arguments?.getString("description")
        Glide.with(this).load(imageUrl).into(binding.storyImageView)
        binding.storyTitleTextView.text = description

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment_layout
            duration = 500
            scrimColor = Color.TRANSPARENT
        }

        postponeEnterTransition()
        binding.root.doOnPreDraw { startPostponedEnterTransition() }


        return binding.root
    }

}