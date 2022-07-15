package com.gscapin.readbookcompose.network

import com.gscapin.readbookcompose.model.GBook
import com.gscapin.readbookcompose.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookApi {

    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query:String): GBook

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId:String): Item
}