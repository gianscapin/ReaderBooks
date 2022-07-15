package com.gscapin.readbookcompose.screens.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gscapin.readbookcompose.data.DataOrException
import com.gscapin.readbookcompose.model.Item
import com.gscapin.readbookcompose.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository): ViewModel() {
    private val listOfBooks: MutableState<DataOrException<List<Item>, Boolean, Exception>> = mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            if(query.isEmpty()){
                return@launch
            }

            listOfBooks.value.loading = true
            listOfBooks.value = repository.getBooks(query)

            if (listOfBooks.value.data.toString().isNotEmpty()){
                listOfBooks.value.loading = false
            }
        }
    }
}