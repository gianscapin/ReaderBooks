package com.gscapin.readbookcompose.model

data class GBook(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)