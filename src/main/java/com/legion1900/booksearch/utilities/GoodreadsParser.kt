package com.legion1900.booksearch.utilities

import android.util.Log
import android.util.Xml
import com.legion1900.booksearch.Author
import com.legion1900.booksearch.Book
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

class GoodreadsParser {

//    fun parse(xml: String): Results {
//        val books = mutableListOf<Book>()
//        val parser = Xml.newPullParser()
//        parser.setInput(StringReader(xml))
//
//
//        do {
//            parser.nextTag()
//        }
//            while (parser.name != "results")
//        val works = readResults(parser)
//    }

    fun parse(xml: String): Triple<Int, Int, Int> {
        val books = mutableListOf<Book>()
        val parser = Xml.newPullParser()
        parser.setInput(StringReader(xml))

//        TODO: Careful with this loop
//        TODO: Use next() instead of nextTag()!!
        while(parser.eventType != XmlPullParser.START_TAG || parser.name != "search") {
            parser.next()
        }
        return readSearch(parser)
    }

    /*
    * Method for reading <query> tag.
    * Order in return triple: results-start, results-end, total-results.
    * Note: smth gone wrong when if (-1, -1, -1) was returned
    * */
    private fun readSearch(parser: XmlPullParser): Triple<Int, Int, Int> {
        Log.d("Testing", "Hello World!")

        val resStart = "results-start"
        val resEnd = "results-end"
        val totalRes = "total-results"
        var start = -1
        var end = -1
        var total = -1
        while (parser.next() != XmlPullParser.END_TAG || parser.name != "search") {
            if (parser.eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    resStart -> start = readNumber(parser)
                    resEnd -> end = readNumber(parser)
                    totalRes -> total = readNumber(parser)
                }
            }
        }
        return Triple(start, end, total)
    }

//    private fun readResults(parser: XmlPullParser): List<Work> {
//        val works = mutableListOf<Work>()
//        while (parser.next() != XmlPullParser.END_TAG && parser.name != "results") {
//            if (parser.name)
//        }
//    }

    /*
    * Method for reading tags which contain only one integer number.
    * */
    private fun readNumber(parser: XmlPullParser): Int {
        parser.next()
        return parser.text.toInt()
    }
}

/*
* Work represents parsed <work> tag.
* */
data class Work(
    val pubYear: Int,
    val pubMonth: Int,
    val pubDay: Int,
    val abgRating: Float,
    val author: Author
)

/*
* Results represent parsed <results> tag.
* */
data class Results(val works: List<Work>, val worksPerPage: Int, val totalWorks: Int)