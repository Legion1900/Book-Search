package com.legion1900.booksearch

import java.util.*

data class Book(
    val id: Int,
    val pubDate: Date,
    val author: Author,
    val avgRating: Float,
    val image: String
)

data class Author(
    val name: String,
    val id: Int
)