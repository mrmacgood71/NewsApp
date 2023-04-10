package it.macgood.newsappapi.ui.di

import it.macgood.data.api.NewsApi
import it.macgood.data.api.Constants
import it.macgood.domain.usecase.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { provideHttpLoggingInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory<NewsApi> { provideNewsApi(get()) }
    single<Retrofit> { provideRetrofit(get()) }
    single<GetNewsUseCase> { GetNewsUseCase(repository = get()) }
    single<SearchNewsUseCase> { SearchNewsUseCase(repository = get()) }
    single<SaveArticleUseCase> { SaveArticleUseCase(repository = get()) }
    single<GetSavedNewsUseCase> { GetSavedNewsUseCase(repository = get()) }
    single<DeleteSavedNewsUseCase> { DeleteSavedNewsUseCase(repository = get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideOkHttpClient(httpInterceptor: HttpLoggingInterceptor) : OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(httpInterceptor).build()
}

fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    return logging
}