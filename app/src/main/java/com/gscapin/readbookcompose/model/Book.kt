package com.gscapin.readbookcompose.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Book(
    @Exclude
    var id: String? = null,
    var title: String? = null,
    var authors: String? = null,
    var notes: String? = null,
    @PropertyName("book_photo_url")
    var photoUrl: String? = null,
    var categories: String? = null,
    @PropertyName("published_date")
    var publishedDate: String? = null,
    var rating: Double? = null,
    var description: String? = null,
    var pageCount: String? = null,
    @PropertyName("started_reading")
    var startedReading: Timestamp? = null,
    @PropertyName("finished_reading")
    var finishedReading: Timestamp? = null,
    var userId: String? = null,
    @PropertyName("google_book_id")
    var googleBookId: String? = null,

    )
