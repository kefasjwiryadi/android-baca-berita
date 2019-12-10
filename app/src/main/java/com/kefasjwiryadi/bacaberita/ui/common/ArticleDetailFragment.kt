package com.kefasjwiryadi.bacaberita.ui.common

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.databinding.ArticleDetailFragmentBinding
import com.kefasjwiryadi.bacaberita.di.Injection
import com.kefasjwiryadi.bacaberita.domain.Article
import com.kefasjwiryadi.bacaberita.util.openWebsiteUrl
import com.kefasjwiryadi.bacaberita.util.share
import com.kefasjwiryadi.bacaberita.util.toDateFormat
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter

class ArticleDetailFragment : Fragment() {

    private lateinit var binding: ArticleDetailFragmentBinding

    private lateinit var article: Article

    private val viewModel: ArticleDetailViewModel by viewModels {
        Injection.provideArticleDetailViewModelFactory(requireContext(), article)
    }

    private var menu: Menu? = null

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

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.articleLd.observe(viewLifecycleOwner, Observer { article ->
            if (article != null) {
                Glide.with(context!!).load(article.urlToImage)
                    .placeholder(R.drawable.image_placeholder)
                    .into(binding.articleDetailImage)
                binding.articleDetailTitle.text = article.title
                binding.articleDetailAuthor.text = article.author
                binding.articleDetailPublishedAt.text =
                    "Diterbitkan: ${article.publishedAt?.toDateFormat()}"
                binding.articleDetailDescription.text = article.description
                binding.articleDetailSource.text = article.source?.name

                // Set content
                when {
                    article.fullContent != null -> {
                        binding.articleDetailContent.setOnClickUrlListener { _, url ->
                            openWebsiteUrl(requireContext(), url)
                            return@setOnClickUrlListener true
                        }
                        binding.articleDetailContent.setHtml(
                            article.fullContent!!,
                            HtmlHttpImageGetter(binding.articleDetailContent)
                        )
                    }
                    article.content != null -> {
                        binding.articleDetailContent.text = article.content
                    }
                    else -> {
                        binding.articleDetailContent.text =
                            "Konten untuk artikel ini tidak tersedia"
                    }
                }

                binding.readMoreButton.setOnClickListener {
                    openWebsiteUrl(context!!, article.url)
                }

                setSaveMenuIcon(article.favorite > 0)
            }
        })

        viewModel.eventToast.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.doneToast()
            }
        })

        (activity as AppCompatActivity).setSupportActionBar(binding.articleDetailToolbar)

        binding.articleDetailToolbar.setupWithNavController(findNavController())
        binding.articleDetailToolbar.title = ""
        binding.articleDetailToolbar.navigationIcon =
            resources.getDrawable(R.drawable.ic_back_detail)

    }

    private fun setSaveMenuIcon(isSaved: Boolean) {
        menu?.findItem(R.id.save_action)?.setIcon(
            if (isSaved) {
                R.drawable.ic_save_detail_fill
            } else {
                R.drawable.ic_save_detail_outline
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        inflater.inflate(R.menu.article_detail_menu, menu)
        if (viewModel.articleLd.value != null) {
            setSaveMenuIcon(viewModel.articleLd.value!!.favorite > 0)
        }
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
