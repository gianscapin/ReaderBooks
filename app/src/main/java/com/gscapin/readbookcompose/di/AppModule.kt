package com.gscapin.readbookcompose.di

import com.gscapin.readbookcompose.network.BookApi
import com.gscapin.readbookcompose.repository.BookRepository
import com.gscapin.readbookcompose.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBookRepository(api: BookApi) = BookRepository(api)

    @Singleton
    @Provides
    fun provideBookApi(): BookApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApi::class.java)
    }
}