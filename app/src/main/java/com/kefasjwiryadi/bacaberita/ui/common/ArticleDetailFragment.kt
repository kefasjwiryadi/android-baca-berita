package com.kefasjwiryadi.bacaberita.ui.common

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide

import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.databinding.ArticleDetailFragmentBinding
import com.kefasjwiryadi.bacaberita.di.Injection
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.ui.MainActivity
import com.kefasjwiryadi.bacaberita.util.openWebsiteUrl
import com.kefasjwiryadi.bacaberita.util.toDateFormat
import com.kefasjwiryadi.bacaberita.util.clearUrl
import com.kefasjwiryadi.bacaberita.util.share

class ArticleDetailFragment : Fragment() {

    private lateinit var binding: ArticleDetailFragmentBinding

    private lateinit var article: Article

    private val viewModel: ArticleDetailViewModel by viewModels {
        Injection.provideArticleDetailViewModelFactory(requireContext(), article)
    }

    private lateinit var menu: Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ArticleDetailFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        article = ArticleDetailFragmentArgs.fromBundle(arguments!!).article

        setHasOptionsMenu(true)

        viewModel.isArticleFavorited.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: $it")
            if (it != null) {
                menu.getItem(1).setIcon(
                    if (it) {
                        R.drawable.ic_save_detail_fill
                    } else {
                        R.drawable.ic_save_detail_outline
                    }
                )
            }
        })

        (activity as AppCompatActivity).setSupportActionBar(binding.articleDetailToolbar)

        binding.articleDetailToolbar.setupWithNavController(findNavController())
        binding.articleDetailToolbar.title = ""
        binding.articleDetailToolbar.navigationIcon =
            resources.getDrawable(R.drawable.ic_back_detail)

        Glide.with(context!!).load(article.urlToImage).placeholder(R.drawable.image_placeholder)
            .into(binding.articleDetailImage)
        binding.articleDetailTitle.text = article.title
        binding.articleDetailContent.text = article.content
        binding.articleDetailAuthor.text = article.author
        binding.articleDetailPublishedAt.text =
            "Diterbitkan: ${article.publishedAt?.toDateFormat()}"
        binding.articleDetailDescription.text = article.description
        binding.articleDetailSource.text = article.source?.name

        binding.readMoreButton.setOnClickListener {
            openWebsiteUrl(context!!, article.url.clearUrl())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        inflater.inflate(R.menu.article_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_action -> article.share(context!!)
            R.id.save_action -> viewModel.saveArticle()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "ArticleDetailFragment"
    }
}
