package com.legion1900.booksearch.paging

import androidx.paging.PageKeyedDataSource
import com.legion1900.booksearch.parser.GoodreadsParser
import com.legion1900.booksearch.parser.Results
import com.legion1900.booksearch.parser.Work
import com.legion1900.booksearch.utilities.buildQuery
import java.net.URL

private const val BOOKS_PER_PAGE = 20

class BookDataSource(private val search: String) : PageKeyedDataSource<Int, Work>() {

    private var totalPages = 0

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Work>
    ) {
        val initialUrl =  buildQuery(search)
        val result = loadNParse(initialUrl)
        val totalItems = result.run { endIndex - startIndex + 1 }
        totalPages = if (totalItems.rem(BOOKS_PER_PAGE) != 0) totalItems / BOOKS_PER_PAGE + 1
        else totalItems / BOOKS_PER_PAGE
        val nextPage = 2
        callback.onResult(result.works, 0, totalItems, null, nextPage)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Work>) {
        if (params.key < totalPages) {
            val url = buildQuery(search, page=params.key)
            val result = loadNParse(url)
            callback.onResult(result.works, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Work>) {
        /*
        * There is no need to load previous nextPage because it's already loaded
        * */
    }

    private fun loadNParse(url: URL): Results {
//       TODO: add network state verification & exception handling
        val xml = url.readText()
        val parser = GoodreadsParser()
        return parser.parse(xml)
    }

    /*
    * Builds initialUrl for next nextPage from current initialUrl
    * */
//    private fun buildNextPageUrl(currUrl: URL, nextPage: Int): URL? {
//        return if (nextPage < totalPages) nextPageQuery(currUrl, nextPage) else null
//    }
}