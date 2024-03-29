package com.legion1900.booksearch.parser

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader
import java.net.URL

private const val TAG_SEARCH = "search"
private const val TAG_RESULTS = "results"
private const val TAG_WORK = "work"
private const val TAG_PUB_YEAR = "original_publication_year"
private const val TAG_PUB_MONTH = "original_publication_month"
private const val TAG_PUB_DAY = "original_publication_day"
private const val TAG_AVG_RATING = "average_rating"
private const val TAG_ID = "id"
private const val TAG_TITLE = "title"
private const val TAG_AUTHOR = "author"
private const val TAG_NAME = "name"
private const val TAG_IMG = "image_url"

class GoodreadsParser {

    fun parse(xml: String): Results {
        val parser = Xml.newPullParser()
        parser.setInput(StringReader(xml))

        skipTo(parser, TAG_SEARCH)
        val (start, end, total) = readQuery(parser)
        val perPage = end - start + 1
        skipTo(parser, TAG_RESULTS)
        val works = readResults(parser, perPage)
        return Results(works, start, end, total)
    }

    /*
    * Method for reading <query> tag.
    * Order in return triple: results-start, results-end, total-results.
    * Note: smth gone wrong when if (-1, -1, -1) was returned
    * */
    private fun readQuery(parser: XmlPullParser): Triple<Int, Int, Int> {
        val resStart = "results-start"
        val resEnd = "results-end"
        val totalRes = "total-results"
        skipTo(parser, resStart)
        val start = readInt(parser)
        skipTo(parser, resEnd)
        val end = readInt(parser)
        skipTo(parser, totalRes)
        val total = readInt(parser)
        return Triple(start, end, total)
    }

    private fun readResults(parser: XmlPullParser, worksNum: Int): List<Work> {
        val works = mutableListOf<Work>()
        for (i in 0 until worksNum) {
            skipTo(parser, TAG_WORK)
            works.add(readWork(parser))
        }
        return works
    }

    private fun readWork(parser: XmlPullParser): Work =
        parser.let {
            skipTo(it, TAG_PUB_YEAR)
            val pubYear = readDate(it)
            skipTo(it, TAG_PUB_MONTH)
            val pubMonth = readDate(it)
            skipTo(it, TAG_PUB_DAY)
            val pubDay = readDate(it)
            skipTo(it, TAG_AVG_RATING)
            val avgRating = readFloat(it)
            skipTo(it, TAG_ID)
            val bookId = readInt(parser)
            skipTo(it, TAG_TITLE)
            val title = it.nextText()
            skipTo(it, TAG_AUTHOR)
            val author = readAuthor(it)
            skipTo(it, TAG_IMG)
            val imgUrl = it.nextText()

            Work(bookId, title, pubYear, pubMonth, pubDay, author, avgRating, imgUrl)
        }

    private fun readAuthor(parser: XmlPullParser): Author =
        parser.let {
            skipTo(it, TAG_ID)
            val id = readInt(it)
            skipTo(it, TAG_NAME)
            val name = it.nextText()
            Author(id, name)
        }

    /*
    * Method for reading <results-start>, <results-end>, <total-results> tags
    * (some of them may have no value with 'nil' attribute set to true
    * */
    private fun readDate(parser: XmlPullParser): Int? =
        if (parser.attributeCount == 1) readInt(parser) else null

    /*
    * Method for reading tags which contain only one INTEGER number.
    * */
    private fun readInt(parser: XmlPullParser) = parser.nextText().toInt()

    /*
    * Method for reading tags which contain only one FLOAT number.
    * */
    private fun readFloat(parser: XmlPullParser) = parser.nextText().toFloat()

    /*
    * Method for moving cursor to tag with tagName name.
    * */
    private fun skipTo(parser: XmlPullParser, tagName: String) {
        while (parser.eventType != XmlPullParser.START_TAG || parser.name != tagName) {
            parser.next()
        }
    }
}
