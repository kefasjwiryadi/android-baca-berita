package com.kefasjwiryadi.bacaberita.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.databinding.ArticleFragmentBinding
import com.kefasjwiryadi.bacaberita.di.Injection
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.ui.common.ArticleAdapter
import com.kefasjwiryadi.bacaberita.ui.common.OnArticleClickListener
import com.kefasjwiryadi.bacaberita.util.share

private const val TAG = "ArticleFragment"

private const val CATEGORY = "category"

class ArticleFragment : Fragment(), OnArticleClickListener {

    private var category: String? = null

    private lateinit var binding: ArticleFragmentBinding

    private val viewModel: ArticleViewModel by viewModels {
        Injection.provideArticleViewModelFactory(context!!, category!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(CATEGORY)
        }
        Log.d(TAG, "onCreate: $category")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: $category")
        binding = ArticleFragmentBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: $category")
        viewModel.onFragmentResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: $category")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = ArticleAdapter(this, R.menu.article_item_menu)

        binding.articleList.adapter = adapter

        binding.articleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1 && adapter.itemCount > 0) {
                    viewModel.onReachEnd()
                }
            }
        })

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d(TAG, "onViewCreated: ${it[0].title}")
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        binding.articleRefresh.setOnRefreshListener {
            viewModel.refreshArticles()
        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    if (!binding.articleRefresh.isRefreshing) {
                        binding.articleLoadingProgressBar.visibility = View.VISIBLE
                    }
                } else {
                    binding.articleRefresh.isRefreshing = false
                    binding.articleLoadingProgressBar.visibility = View.GONE
                }
            }
        })

    }

    companion object {
        fun newInstance(category: String) =
            ArticleFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY, category)
                }
            }
    }

    override fun onArticleClick(article: Article) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeDestToArticleDetailDest(
                article
            )
        )
    }

    override fun onPopupMenuClick(view: View, article: Article) {
        val menu = PopupMenu(requireContext(), view)
        menu.inflate(R.menu.article_item_menu)

        menu.menu.removeItem(
            if (article.favorite > 0) {
                R.id.save_action
            } else {
                R.id.remove_action
            }
        )

        menu.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.share_action -> {
                    article.share(requireContext())
                }
                R.id.save_action -> {
                    viewModel.addFavoriteArticle(article)
                    Toast.makeText(context, "Artikel tersimpan ke favorit", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.remove_action -> {
                    viewModel.removeFavoriteArticle(article)
                    Toast.makeText(context, "Artikel dihapus dari favorit", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            return@setOnMenuItemClickListener true
        }
        menu.show()
    }
}
