package it.macgood.newsappapi.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import it.macgood.newsappapi.R
import it.macgood.newsappapi.database.ArticleDatabase
import it.macgood.newsappapi.databinding.ActivityNewsBinding
import it.macgood.newsappapi.repository.NewsRepository
import it.macgood.newsappapi.repository.NewsViewModelFactory
import it.macgood.newsappapi.repository.factory

// TODO:
//  1. splash
class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.newsNavHostFragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.breakingNewsFragment, R.id.searchNewsFragment, R.id.savedNewsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}