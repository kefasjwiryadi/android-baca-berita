package com.kefasjwiryadi.bacaberita.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.databinding.MainActivityBinding
import com.kefasjwiryadi.bacaberita.util.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private var lastBottomNavSelectedTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        if (savedInstanceState == null) {
            setupBottomNavigation()
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {

        val navGraphIds = listOf(R.navigation.home_nav, R.navigation.search_nav, R.navigation.favorite_nav)

        val controller = binding.mainBottomNav.setupWithNavController(
            navGraphIds,
            supportFragmentManager,
            R.id.nav_host_container,
            intent
        )

        controller.observe(this, Observer { currentNavController ->
            currentNavController.addOnDestinationChangedListener { _, dest, _ ->
                if (dest.id == R.id.article_detail_dest) {
                    hideBottomNavigation()
                } else {
                    showBottomNavigation()
                }
            }
        })

        binding.mainBottomNav.setOnNavigationItemReselectedListener {
            Log.d(TAG, "setupBottomNavigation: reselected: $it")
        }

    }

    private fun hideBottomNavigation() {
        if (binding.mainBottomNav.visibility == View.VISIBLE) {
            val constrainSet = ConstraintSet()
            constrainSet.clone(binding.mainConstraintLayout)
            constrainSet.connect(
                R.id.nav_host_container,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            constrainSet.applyTo(binding.mainConstraintLayout)

            // bottom_navigation is BottomNavigationView
            val anim = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    binding.mainBottomNav.visibility = View.GONE
                }

                override fun onAnimationStart(animation: Animation?) {

                }

            })
            binding.mainBottomNav.startAnimation(anim)
        }
    }

    private fun showBottomNavigation() {
        if (binding.mainBottomNav.visibility == View.GONE) {
            val constrainSet = ConstraintSet()
            constrainSet.clone(binding.mainConstraintLayout)
            constrainSet.connect(
                R.id.nav_host_container,
                ConstraintSet.BOTTOM,
                R.id.main_bottom_nav,
                ConstraintSet.TOP
            )

            val anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    constrainSet.applyTo(binding.mainConstraintLayout)
                    binding.mainBottomNav.visibility = View.VISIBLE
                }

                override fun onAnimationStart(animation: Animation?) {
                    binding.mainBottomNav.visibility = View.VISIBLE
                }

            })
            binding.mainBottomNav.startAnimation(anim)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}
