package it.macgood.newsappapi.ui.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.macgood.domain.model.Article
import it.macgood.newsappapi.databinding.ItemArticlePreviewBinding
import java.net.URL

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticlePreviewBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        with(holder.binding) {
            holder.itemView.apply {
                Glide.with(this)
                    .load(article.urlToImage)
                    .into(holder.binding.articleImageView)


                val split = article.url.split("/")

                Glide.with(this)
                    .load(URL("${split[0]}//${split[2]}/favicon.ico"))
                    .into(sourceImage)
            }
            sourceTextView.text = article.source.name
            titleTextView.text = article.title
            descriptionTextView.text = article.description
            publishedAtTextView.text = article.publishedAt

            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ArticleViewHolder(
        val binding: ItemArticlePreviewBinding
    ) : RecyclerView.ViewHolder(binding.root)

}