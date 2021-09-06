package com.sanislo.vodapp.di

import com.google.gson.Gson
import com.sanislo.vodapp.data.ApiService
import com.sanislo.vodapp.domain.repo.VodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun providesApiService(): ApiService {
        return Retrofit.Builder().apply {
            baseUrl(ApiService.BASE_URL)
            addConverterFactory(GsonConverterFactory.create(Gson()))
            client(OkHttpClient())
        }.build().create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideVodRepository(
        apiService: ApiService,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ) = VodRepository(apiService, dispatcher)
}
