package com.gscapin.readbookcompose.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gscapin.readbookcompose.data.DataOrException
import com.gscapin.readbookcompose.model.Book
import com.gscapin.readbookcompose.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    val data: MutableState<DataOrException<List<Book>, Boolean, Exception>> = mutableStateOf(
        DataOrException(listOf(), true, Exception(""))
    )

    init {
        getAllBooks()
    }

    private fun getAllBooks() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllBooks()

            if(!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
    }
}