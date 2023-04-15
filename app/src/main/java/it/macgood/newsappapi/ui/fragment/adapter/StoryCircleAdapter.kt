package it.macgood.newsappapi.ui.fragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.macgood.domain.model.Article
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.ItemStoryCircleBinding

// TODO: 1. pull images from api
class StoryCircleAdapter(
    private val previewImages: List<Int>
) : RecyclerView.Adapter<StoryCircleAdapter.StoryCircleViewHolder>() {

    private val differCallback = object: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryCircleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return StoryCircleViewHolder(ItemStoryCircleBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: StoryCircleViewHolder, position: Int) {
        val story = differ.currentList[position]
        with(holder) {
            Glide.with(itemView)
                .load(story.urlToImage)
                .into(binding.circleStoryImageView)

            binding.circleStoryImageView.setOnClickListener {
                itemView.findNavController().navigate(R.id.action_breakingNewsFragment_to_storyFragment, bundleOf(
                    "imageUrl" to story.urlToImage,
                    "sourceName" to story.source.name,
                    "description" to story.description
                ))
            }

            binding.circleStoryTextView.text = story.source.name
//            binding.circleStoryImageView.borderColor
        }

    }

    override fun getItemCount(): Int = previewImages.size

    inner class StoryCircleViewHolder(val binding: ItemStoryCircleBinding)
        : RecyclerView.ViewHolder(binding.root)
    
}