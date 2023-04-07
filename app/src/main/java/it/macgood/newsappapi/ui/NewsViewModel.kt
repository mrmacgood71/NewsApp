package it.macgood.newsappapi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.macgood.newsappapi.model.NewsResponse
import it.macgood.newsappapi.repository.NewsRepository
import it.macgood.newsappapi.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    private val _breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNews: LiveData<Resource<NewsResponse>> = _breakingNews

    var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
//    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews
    var searchNewsPage = 1

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success<NewsResponse>(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success<NewsResponse>(it)
            }
        }
        return Resource.Error(response.message())
    }

}