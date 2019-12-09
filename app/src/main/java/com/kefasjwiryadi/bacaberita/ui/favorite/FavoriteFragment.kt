package com.kefasjwiryadi.bacaberita.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.kefasjwiryadi.bacaberita.databinding.FavoriteFragmentBinding
import com.kefasjwiryadi.bacaberita.di.Injection
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.ui.common.ArticleAdapter
import com.kefasjwiryadi.bacaberita.ui.common.OnArticleClickListener
import com.kefasjwiryadi.bacaberita.util.showPopupMenu

class FavoriteFragment : Fragment(), OnArticleClickListener {

    val viewModel: FavoriteViewModel by viewModels {
        Injection.provideFavoriteViewModelFactory(requireContext())
    }

    private lateinit var binding: FavoriteFragmentBinding

    private var itemInsertedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FavoriteFragmentBinding.inflate(layoutInflater, container, false)
        Log.d(TAG, "onCreateView: favorite")
        itemInsertedOnce = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: favorite")

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.favoriteToolbar.apply {
            setupWithNavController(findNavController())
            title = "Favorit"
        }

        val adapter = ArticleAdapter(this, ArticleAdapter.SMALL_LAYOUT)
        binding.favoriteArticleList.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                Log.d(TAG, "favorite onItemRangeInserted: $positionStart $itemCount")
                if (itemInsertedOnce) {
                    // Scroll to top if there is a new article added to favorite
                    binding.favoriteArticleList.scrollToPosition(positionStart)
                } else {
                    itemInsertedOnce = true
                }
            }

            override fun onChanged() {
                super.onChanged()
                Log.d(TAG, "favorite onChanged: ")
            }
        })

        viewModel.favoriteArticles.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.submitList(it)
            }
        })
    }

    override fun onArticleClick(article: Article) {
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteDestToArticleDetailFragment(article)
        )
    }

    override fun onPopupMenuClick(view: View, article: Article) {
        article.showPopupMenu(view, {
            viewModel.addFavoriteArticle(it)
        }, {
            viewModel.removeFavoriteArticle(it)
        })
    }

    companion object {
        private const val TAG = "FavoriteFragment"
    }

}
