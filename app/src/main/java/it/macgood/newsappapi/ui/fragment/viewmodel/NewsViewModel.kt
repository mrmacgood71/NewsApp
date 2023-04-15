package it.macgood.newsappapi.ui.fragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.macgood.data.repository.SavedNewsRepositoryImpl
import it.macgood.domain.model.Article as ArticleDto
import it.macgood.domain.model.NewsResponse
import it.macgood.domain.model.Source
import it.macgood.domain.usecase.*
import it.macgood.newsappapi.utils.Resource
import it.macgood.newsappapi.utils.toDataArticle
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val getNewsBySourceUseCase: GetNewsBySourceUseCase,
    private val savedNewsRepositoryImpl: SavedNewsRepositoryImpl
) : ViewModel() {

    private val _breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNews: LiveData<Resource<NewsResponse>> = _breakingNews
    var breakingNewsPage = 1

    var breakingNewsResponse: NewsResponse? = null

    private val _searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    val url: MutableLiveData<String> by lazy { MutableLiveData() }

    val sourceNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

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

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = getNewsUseCase.execute(countryCode, breakingNewsPage)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun getNewsBySource(source: String) = viewModelScope.launch {
        sourceNews.postValue(Resource.Loading())
        val response = getNewsBySourceUseCase.execute(source)
        sourceNews.postValue(handleSourceNewsResponse(response))
    }

    private fun getSourceIcons(source: String) = viewModelScope.launch {

    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = it
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)

                }
                return Resource.Success<NewsResponse>(breakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = it
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)

                }
                return Resource.Success<NewsResponse>(searchNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSourceNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success<NewsResponse>(it)
            }
        }

        return Resource.Error<NewsResponse>(response.message())
    }

}