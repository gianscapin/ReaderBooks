package com.gscapin.readbookcompose.repository

import com.gscapin.readbookcompose.data.DataOrException
import com.gscapin.readbookcompose.data.Resource
import com.gscapin.readbookcompose.model.Item
import com.gscapin.readbookcompose.network.BookApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BookApi) {

    suspend fun getBooks(searchQuery: String): Resource<List<Item>>{
        return  try {
            Resource.Loading(true)
            val items = api.getAllBooks(searchQuery)
            Resource.Success(data = items.items)
        }catch (e: Exception){
            return Resource.Error(message = e.message)
        }

    }

    suspend fun getBookInfo(bookId: String): Resource<Item>{
        val response =  try{
            Resource.Loading(true)
            api.getBookInfo(bookId)

        }catch (e: Exception){
            return Resource.Error(message = e.message)
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }
}