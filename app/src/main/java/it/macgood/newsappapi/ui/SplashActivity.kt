package it.macgood.newsappapi.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import it.macgood.justnav.viewBinding
import it.macgood.newsappapi.R
import it.macgood.newsappapi.databinding.ActivitySplashBinding
import it.macgood.newsappapi.utils.Resource
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val newsViewModel: NewsViewModel by viewModel()
        newsViewModel.breakingNews.observe(this) { response ->
            when(response) {
                is Resource.Success -> {
                    val intent = Intent(this, NewsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}