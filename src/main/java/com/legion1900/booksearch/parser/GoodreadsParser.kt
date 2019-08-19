package com.legion1900.booksearch.parser

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader
import java.net.URL
import java.util.*

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
        val test = Xml.newPullParser()
        test.setInput(StringReader("<work>\n" +
                "<id type=\"integer\">7593882</id>\n" +
                "<books_count type=\"integer\">17</books_count>\n" +
                "<ratings_count type=\"integer\">926</ratings_count>\n" +
                "<text_reviews_count type=\"integer\">123</text_reviews_count>\n" +
                "<original_publication_year type=\"integer\">2010</original_publication_year>\n" +
                "<original_publication_month type=\"integer\">3</original_publication_month>\n" +
                "<original_publication_day type=\"integer\">31</original_publication_day>\n" +
                "<average_rating>3.95</average_rating>\n" +
                "<best_book type=\"Book\">\n" +
                "<id type=\"integer\">11309025</id>\n" +
                "<title>A Pleasure to Burn: Fahrenheit 451 Stories</title>\n" +
                "<author>\n" +
                "<id type=\"integer\">1630</id>\n" +
                "<name>Ray Bradbury</name>\n" +
                "</author>\n" +
                "<image_url>\n" +
                "https://s.gr-assets.com/assets/nophoto/book/111x148-bcc042a9c91a29c1d680899eff700a03.png\n" +
                "</image_url>\n" +
                "<small_image_url>\n" +
                "https://s.gr-assets.com/assets/nophoto/book/50x75-a91bf249278a81aabab721ef782c4a74.png\n" +
                "</small_image_url>\n" +
                "</best_book>\n" +
                "</work>\n" +
                "<work>\n" +
                "<id type=\"integer\">40279</id>\n" +
                "<books_count type=\"integer\">2</books_count>\n" +
                "<ratings_count type=\"integer\">953</ratings_count>\n" +
                "<text_reviews_count type=\"integer\">48</text_reviews_count>\n" +
                "<original_publication_year type=\"integer\">2000</original_publication_year>\n" +
                "<original_publication_month type=\"integer\">12</original_publication_month>\n" +
                "<original_publication_day type=\"integer\" nil=\"true\"/>\n" +
                "<average_rating>4.19</average_rating>\n" +
                "<best_book type=\"Book\">\n" +
                "<id type=\"integer\">40694</id>\n" +
                "<title>Ray Bradbury's Fahrenheit 451</title>\n" +
                "<author>\n" +
                "<id type=\"integer\">236</id>\n" +
                "<name>Harold Bloom</name>\n" +
                "</author>\n" +
                "<image_url>\n" +
                "https://s.gr-assets.com/assets/nophoto/book/111x148-bcc042a9c91a29c1d680899eff700a03.png\n" +
                "</image_url>\n" +
                "<small_image_url>\n" +
                "https://s.gr-assets.com/assets/nophoto/book/50x75-a91bf249278a81aabab721ef782c4a74.png\n" +
                "</small_image_url>\n" +
                "</best_book>\n" +
                "</work>"))
        val works = readResults(test, 2)
        Log.d("Testing", "There is ${works.size} books")
        return Results(works, 1, 1)
//        val parser = Xml.newPullParser()
//        parser.setInput(StringReader(xml))
//
//        skipTo(parser, TAG_SEARCH)
//        val (start, end, total) = readQuery(parser)
//        val perPage = start - end
//        skipTo(parser, TAG_RESULTS)
//        Log.d("Testing", "Before readResults call")
//        val works = readResults(parser)
//        return Results(works, perPage, total)
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
//        TODO: change parser.name condition check
        for (i in 0 until worksNum) {
            if (parser.eventType == XmlPullParser.END_DOCUMENT) break
            skipTo(parser, TAG_WORK)
            works.add(readWork(parser))
        }
        return works
    }

    private fun readWork(parser: XmlPullParser): Work =
        parser.let {
            Log.d("Testing", "<work> reading started")

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
            val imgUrl = URL(it.nextText())

            Log.d("Testing", "<work> was read")
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
    * Method for moving cursor to tag with tagName name.
    * */
    private fun skipTo(parser: XmlPullParser, tagName: String) {
//        var a = 1
        while (parser.eventType != XmlPullParser.START_TAG || parser.name != tagName) {
//            val event = when (parser.eventType) {
//                2 -> "start tag"
//                4 -> "text"
//                3 -> "end tag"
//                1 -> "end document"
//                else -> "other event"
//            }
//            Log.d("Testing", "skipTo loop: $event || ${parser.name}")
//            Log.d("Testing", "In loop for ${a++} cycle")
            parser.next()
        }
    }

    /*
    * Method for reading tags which contain only one INTEGER number.
    * */
    private fun readInt(parser: XmlPullParser) = parser.nextText().toInt()

    /*
    * Method for reading tags which contain only one FLOAT number.
    * */
    private fun readFloat(parser: XmlPullParser) = parser.nextText().toFloat()

    /*
    * Method for reading <results-start>, <results-end>, <total-results> tags
    * (some of them may have no value with 'nil' attribute set to true
    * */
    private fun readDate(parser: XmlPullParser): Int? = if (parser.attributeCount == 1) readInt(parser) else null
}
