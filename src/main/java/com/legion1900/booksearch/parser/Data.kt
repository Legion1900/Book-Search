package com.legion1900.booksearch.parser

import java.net.URL
import java.util.*

/*
* Represents parsed <author> tag
* */
data class Author(
    val id: Int,
    val name: String
)

/*
* Represents parsed <work> tag;
* <work> ~ info about book
* */

data class Work(
    val id: Int,
    val title: String,
    val pubYear: Int?,
    val pubMonth: Int?,
    val pubDay: Int?,
    val author: Author,
    val avgRating: Float,
    val imgUrl: URL
)

/*
* Represent parsed <results> tag.
* */
data class Results(val works: List<Work>, val worksPerPage: Int, val totalWorks: Int)