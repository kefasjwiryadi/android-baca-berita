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

    private val articleDetailViewModel: ArticleDetailViewModel by viewModels {
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

        binding.apply {
            viewModel = articleDetailViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        setupToolbar()

        subscribeUi()
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)

        binding.articleDetailToolbar.apply {
            (activity as AppCompatActivity).setSupportActionBar(this)
            setupWithNavController(findNavController())
            title = ""
            navigationIcon = resources.getDrawable(R.drawable.ic_back_detail)
        }
    }

    private fun subscribeUi() {
        articleDetailViewModel.apply {
            articleLd.observe(viewLifecycleOwner, Observer { article ->
                if (article != null) {
                    Glide.with(context!!).load(article.urlToImage)
                        .placeholder(R.drawable.image_placeholder)
                        .into(binding.articleDetailImage)
                    binding.apply {
                        articleDetailTitle.text = article.title
                        articleDetailAuthor.text = article.author
                        articleDetailPublishedAt.text = getString(
                            R.string.published_at,
                            article.publishedAt?.toDateFormat()
                        )
                        articleDetailDescription.text = article.description
                        articleDetailSource.text = article.source?.name

                        // Set content
                        when {
                            article.fullContent != null -> {
                                articleDetailContent.setOnClickUrlListener { _, url ->
                                    openWebsiteUrl(requireContext(), url)
                                    return@setOnClickUrlListener true
                                }
                                articleDetailContent.setHtml(
                                    article.fullContent!!,
                                    HtmlHttpImageGetter(articleDetailContent)
                                )
                            }
                            article.content != null -> {
                                articleDetailContent.text = article.content
                            }
                            else -> {
                                articleDetailContent.text =
                                    getString(R.string.content_not_available)
                            }
                        }

                        readMoreButton.setOnClickListener {
                            openWebsiteUrl(context!!, article.url)
                        }

                        setSaveMenuIcon(article.favorite > 0)
                    }

                }
            })

            eventToast.observe(viewLifecycleOwner, Observer {
                if (it != 0) {
                    Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
                    articleDetailViewModel.doneToast()
                }
            })
        }
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
        if (articleDetailViewModel.articleLd.value != null) {
            setSaveMenuIcon(articleDetailViewModel.articleLd.value!!.favorite > 0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_action -> article.share(context!!)
            R.id.save_action -> articleDetailViewModel.saveArticle()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "ArticleDetailFragment"
    }
}
