package it.macgood.newsappapi.ui.activity

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import it.macgood.justnav.viewBinding
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNewsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.newsNavHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)
        setupBottomNavigationViewColors()
    }

    private fun setupBottomNavigationViewColors() {
        var states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )

        var colors = intArrayOf(
            resources.getColor(R.color.darkBlueLogo),
            resources.getColor(R.color.lightOrangeLogo)
        )

        var colorStateList = ColorStateList(states, colors)

        binding.bottomNavigationView.itemTextColor = colorStateList
        binding.bottomNavigationView.itemIconTintList = colorStateList
        binding.bottomNavigationView.itemRippleColor = colorStateList
    }
}