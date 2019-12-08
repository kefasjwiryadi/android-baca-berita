package com.kefasjwiryadi.bacaberita.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kefasjwiryadi.bacaberita.databinding.SearchFragmentBinding
import com.kefasjwiryadi.bacaberita.di.Injection
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.ui.common.ArticleAdapter
import com.kefasjwiryadi.bacaberita.ui.common.OnArticleClickListener
import com.kefasjwiryadi.bacaberita.util.showPopupMenu

class SearchFragment : Fragment(), OnArticleClickListener {

    private lateinit var binding: SearchFragmentBinding

    private val viewModel: SearchViewModel by viewModels {
        Injection.provideSearchViewModelFactory(requireContext())
    }

    private var lastQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = SearchFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    private var currentPopupView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchArticles(query)
                    lastQuery = query
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        val adapter = ArticleAdapter(this, ArticleAdapter.SMALL_LAYOUT)

        binding.searchArticleList.adapter = adapter

        binding.searchArticleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1 && adapter.itemCount > 0) {
                    viewModel.onReachEnd()
                }
            }
        })

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d(TAG, "onViewCreated: ${it.size}")
                adapter.submitList(it)
            }
        })

        viewModel.eventShowPopupMenu.observe(viewLifecycleOwner, Observer { article ->
            if (article != null) {
                currentPopupView?.let {
                    article.showPopupMenu(it, {
                        viewModel.addFavoriteArticle(article)
                    }, {
                        viewModel.removeFavoriteArticle(article)
                    })
                }
                currentPopupView = null
                viewModel.setSelectedArticle(null)
            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            it?.let {

            }
        })

        binding.searchRetryButton.setOnClickListener {
            viewModel.searchArticles(lastQuery)
        }

    }

    override fun onArticleClick(article: Article) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchDestToArticleDetailDest(
                article
            )
        )
    }

    override fun onPopupMenuClick(view: View, article: Article) {
        currentPopupView = view
        viewModel.setSelectedArticle(article)
    }

    companion object {
        private const val TAG = "SearchFragment"
    }

}
