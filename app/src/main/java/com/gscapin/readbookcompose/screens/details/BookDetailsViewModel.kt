package com.gscapin.readbookcompose.screens.details

import androidx.lifecycle.ViewModel
import com.gscapin.readbookcompose.data.Resource
import com.gscapin.readbookcompose.model.Item
import com.gscapin.readbookcompose.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(private val repository: BookRepository): ViewModel(){
    suspend fun getBookInfo(bookId: String): Resource<Item> = repository.getBookInfo(bookId)

}