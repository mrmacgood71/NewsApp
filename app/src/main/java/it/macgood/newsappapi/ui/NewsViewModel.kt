package it.macgood.newsappapi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.macgood.data.repository.SavedNewsRepositoryImpl
import it.macgood.domain.model.Article as ArticleDto
import it.macgood.domain.model.NewsResponse
import it.macgood.domain.usecase.*
import it.macgood.newsappapi.utils.Resource
import it.macgood.newsappapi.utils.toDataArticle
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val savedNewsRepositoryImpl: SavedNewsRepositoryImpl
) : ViewModel() {

    private val _breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNews: LiveData<Resource<NewsResponse>> = _breakingNews
    var breakingNewsPage = 1

    private val _searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews
    var searchNewsPage = 1

    val url: MutableLiveData<String> by lazy { MutableLiveData() }

    init {
        getBreakingNews("us")
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = searchNewsUseCase.execute(searchQuery, searchNewsPage)
        _searchNews.postValue(handleSearchNewsResponse(response))
    }

    fun saveArticle(articleDto: ArticleDto) = viewModelScope.launch {
        savedNewsRepositoryImpl.upsert(articleDto.toDataArticle())
    }

    fun getSavedNews() = savedNewsRepositoryImpl.getAllArticles()


    fun deleteArticle(articleDto: ArticleDto) = viewModelScope.launch {
        savedNewsRepositoryImpl.deleteArticle(articleDto.toDataArticle())
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = getNewsUseCase.execute(countryCode, breakingNewsPage)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success<NewsResponse>(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success<NewsResponse>(it)
            }
        }
        return Resource.Error(response.message())
    }

}