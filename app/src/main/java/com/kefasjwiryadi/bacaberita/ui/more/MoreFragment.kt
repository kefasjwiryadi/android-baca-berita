package com.kefasjwiryadi.bacaberita.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kefasjwiryadi.bacaberita.R
import com.kefasjwiryadi.bacaberita.databinding.MoreFragmentBinding

/**
 * A simple [Fragment] subclass.
 */
class MoreFragment : Fragment() {

    private lateinit var binding: MoreFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MoreFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        binding.moreContactText.setOnClickListener {
            composeEmail()
        }
    }

    private fun composeEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
        }
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun setupToolbar() {
        binding.moreToolbar.apply {
            (activity as AppCompatActivity).setSupportActionBar(this)
            setupWithNavController(findNavController())
            title = getString(R.string.more)
        }
    }

}
