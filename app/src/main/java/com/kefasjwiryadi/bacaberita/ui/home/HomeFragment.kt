package com.kefasjwiryadi.bacaberita.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kefasjwiryadi.bacaberita.BuildConfig
import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.databinding.HomeFragmentBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated: ")

        setupToolbar()

        setupViewPager()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.homeToolbar)
        binding.homeToolbar.apply {
            setupWithNavController(findNavController())
            title =
                resources.getString(R.string.app_name) + if (BuildConfig.DEBUG) " (Debug)" else ""
        }
    }

    private fun setupViewPager() {
        // Create adapter for viewpager
        binding.homeViewpager.adapter = HomePagerAdapter(childFragmentManager)

        // Set text for each tab
        binding.homeTablayout.setupWithViewPager(binding.homeViewpager)
    }

}
