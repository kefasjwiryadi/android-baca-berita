package com.kefasjwiryadi.bacaberita.ui.search

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.databinding.SearchFragmentBinding
import com.kefasjwiryadi.bacaberita.di.Injection
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.ui.common.ArticleAdapter
import com.kefasjwiryadi.bacaberita.ui.common.OnArticleClickListener

class SearchFragment : Fragment(), OnArticleClickListener {

    private lateinit var binding: SearchFragmentBinding

    private val viewModel: SearchViewModel by viewModels {
        Injection.provideSearchViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = SearchFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchArticles(query)
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
                adapter.submitList(it)
            }
        })

    }

    override fun onArticleClick(article: Article) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchDestToArticleDetailDest(
                article
            )
        )
    }

    override fun onPopupMenuClick(view: View, article: Article) {

    }

}
