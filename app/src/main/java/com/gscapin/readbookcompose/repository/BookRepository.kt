package com.gscapin.readbookcompose.repository

import com.gscapin.readbookcompose.data.DataOrException
import com.gscapin.readbookcompose.model.Item
import com.gscapin.readbookcompose.network.BookApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BookApi) {
    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
    private val dataBookOrException = DataOrException<Item, Boolean, Exception>()
    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllBooks(searchQuery).items
            if(dataOrException.data!!.isNotEmpty()) dataOrException.loading = false
        }catch (e: Exception){
            dataOrException.e = e
        }

        return dataOrException
    }

    suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception>{
        try {
            dataBookOrException.loading = true
            dataBookOrException.data = api.getBookInfo(bookId)
            if(dataBookOrException.data.toString().isNotEmpty()) dataBookOrException.loading = false
        }catch (e: Exception){
            dataBookOrException.e = e
        }

        return dataBookOrException
    }
}