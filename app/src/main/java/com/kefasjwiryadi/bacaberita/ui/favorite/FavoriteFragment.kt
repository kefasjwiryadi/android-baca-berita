package com.kefasjwiryadi.bacaberita.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kefasjwiryadi.bacaberita.databinding.FavoriteFragmentBinding
import com.kefasjwiryadi.bacaberita.di.Injection
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.ui.common.ArticleAdapter
import com.kefasjwiryadi.bacaberita.ui.common.OnArticleClickListener

class FavoriteFragment : Fragment(), OnArticleClickListener {

    val viewModel: FavoriteViewModel by viewModels {
        Injection.provideFavoriteViewModelFactory(requireContext())
    }

    private lateinit var binding: FavoriteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FavoriteFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoriteToolbar.apply {
            setupWithNavController(findNavController())
            title = "Favorit"
        }

        val adapter = ArticleAdapter(this, ArticleAdapter.SMALL_LAYOUT)
        binding.favoriteArticleList.adapter = adapter

//        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
//                super.onItemRangeChanged(positionStart, itemCount)
//                binding.favoriteArticleList.scrollToPosition(positionStart)
//                Log.d(TAG, "onItemRangeChanged: $positionStart")
//            }
//
//            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                super.onItemRangeInserted(positionStart, itemCount)
//                binding.favoriteArticleList.scrollToPosition(positionStart)
//                Log.d(TAG, "onItemRangeInserted: $positionStart $itemCount")
//            }
//
//        })

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

    }

    companion object {
        private const val TAG = "FavoriteFragment"
    }

}
