package com.gscapin.readbookcompose.screens.update

import androidx.lifecycle.ViewModel
import com.gscapin.readbookcompose.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookUpdateViewModel @Inject constructor(private val repository: BookRepository): ViewModel(){

}
