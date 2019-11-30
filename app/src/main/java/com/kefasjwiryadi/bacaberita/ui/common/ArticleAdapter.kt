package com.kefasjwiryadi.bacaberita.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.databinding.ArticleItemBinding
import com.kefasjwiryadi.bacaberita.databinding.SmallArticleItemBinding
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.util.share
import com.kefasjwiryadi.bacaberita.util.toPrettyTime


class ArticleAdapter(
    private val onArticleClickListener: OnArticleClickListener,
    private val layoutType: Int = NORMAL_LAYOUT
) :
    ListAdapter<Article, AbstractArticleViewHolder>(ArticleDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractArticleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (layoutType) {
            SMALL_LAYOUT -> {
                SmallArticleViewHolder(
                    SmallArticleItemBinding.inflate(layoutInflater, parent, false),
                    onArticleClickListener
                )
            }
            else -> {
                ArticleViewHolder(
                    ArticleItemBinding.inflate(layoutInflater, parent, false),
                    onArticleClickListener
                )
            }
        }
    }

    override fun onBindViewHolder(holder: AbstractArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        const val SMALL_LAYOUT = 1
        const val NORMAL_LAYOUT = 0
    }
}

abstract class AbstractArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(article: Article)
}

class ArticleViewHolder(
    private val binding: ArticleItemBinding,
    private val onArticleClickListener: OnArticleClickListener
) : AbstractArticleViewHolder(binding.root) {

    override fun bind(article: Article) {
        binding.articleItemTitle.text = article.title
        binding.articleItemSource.text = article.source?.name
        binding.articleItemPublishedAt.text = article.publishedAt?.toPrettyTime()
        Glide.with(binding.root).load(article.urlToImage).placeholder(
            binding.articleItemImage.context.resources.getDrawable(
                R.drawable.image_placeholder
            )
        )
            .into(binding.articleItemImage)
        binding.articleItemMoreButton.setOnClickListener {
            onArticleClickListener.onPopupMenuClick(it, article)
        }
        binding.root.setOnClickListener {
            onArticleClickListener.onArticleClick(article)
        }
    }
}

private fun inflatePopupMenu(context: Context, view: View, menuLayout: Int, article: Article) {
    val popupMenu = PopupMenu(context, view)
    popupMenu.inflate(menuLayout)
    popupMenu.setOnMenuItemClickListener {
        when (it.itemId) {
            R.id.share_action -> {
                article.share(context)
            }
        }
        return@setOnMenuItemClickListener true
    }
    popupMenu.show()
}

class SmallArticleViewHolder(
    private val binding: SmallArticleItemBinding,
    private val onArticleClickListener: OnArticleClickListener
) : AbstractArticleViewHolder(binding.root) {

    override fun bind(article: Article) {
        binding.smallArticleItemTitle.text = article.title
        binding.smallArticleItemSource.text = article.source?.name
        binding.smallArticleItemPublishedAt.text = article.publishedAt?.toPrettyTime()
        Glide.with(binding.root).load(article.urlToImage).placeholder(
            binding.smallArticleItemImage.context.resources.getDrawable(
                R.drawable.image_placeholder
            )
        )
            .into(binding.smallArticleItemImage)

        binding.smallArticleItemMoreButton.setOnClickListener {
            onArticleClickListener.onPopupMenuClick(it, article)
        }

        binding.root.setOnClickListener {
            onArticleClickListener.onArticleClick(article)
        }
    }

}

class ArticleDiffCallBack : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

}

interface OnArticleClickListener {
    fun onArticleClick(article: Article)
    fun onPopupMenuClick(view: View, article: Article)
}