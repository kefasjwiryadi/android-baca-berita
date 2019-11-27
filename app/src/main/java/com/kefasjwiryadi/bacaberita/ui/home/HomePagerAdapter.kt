package com.kefasjwiryadi.bacaberita.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kefasjwiryadi.bacaberita.network.NewsApiService
import com.kefasjwiryadi.bacaberita.util.translate

class HomePagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    companion object {
        val TAB_LIST = listOf(
            NewsApiService.CATEGORY_GENERAL,
            NewsApiService.CATEGORY_ENTERTAINMENT,
            NewsApiService.CATEGORY_SPORTS,
            NewsApiService.CATEGORY_BUSINESS,
            NewsApiService.CATEGORY_TECHNOLOGY,
            NewsApiService.CATEGORY_HEALTH,
            NewsApiService.CATEGORY_SCIENCE
        )
    }

    override fun getItem(position: Int): Fragment {
        return ArticleFragment.newInstance(TAB_LIST[position])
    }

    override fun getCount(): Int {
        return TAB_LIST.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_LIST[position].translate()
    }
}