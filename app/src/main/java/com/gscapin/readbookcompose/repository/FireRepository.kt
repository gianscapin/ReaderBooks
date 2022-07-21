package com.gscapin.readbookcompose.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.gscapin.readbookcompose.data.DataOrException
import com.gscapin.readbookcompose.model.Book
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.Exception

class FireRepository @Inject constructor(
    private val queryBook: Query
) {

    suspend fun getAllBooks(): DataOrException<List<Book>, Boolean, Exception> {

        val dataOrException = DataOrException<List<Book>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(Book::class.java)!!
            }
            if(!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false

        }catch (e: FirebaseFirestoreException){
            dataOrException.e = e
        }

        return dataOrException
    }
}