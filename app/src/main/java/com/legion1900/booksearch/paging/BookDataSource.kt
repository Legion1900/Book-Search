package com.legion1900.booksearch.paging

import android.net.Uri
import androidx.paging.PageKeyedDataSource
import com.legion1900.booksearch.parser.GoodreadsParser
import com.legion1900.booksearch.parser.Results
import com.legion1900.booksearch.parser.Work
import com.legion1900.booksearch.utilities.nextPageQuery
import java.lang.StringBuilder
import java.net.URL

private const val BOOKS_PER_PAGE = 20

class BookDataSource(private val initialUrl: URL) : PageKeyedDataSource<URL, Work>() {

    private var nextPage = 2
        get() = field++
        set(value) {}

    private var totalPages = 0

    override fun loadInitial(
        params: LoadInitialParams<URL>,
        callback: LoadInitialCallback<URL, Work>
    ) {
        val result = loadNParse(initialUrl)
        val totalCount = result.run { endIndex - startIndex + 1 }
        totalPages =
            if (totalCount.rem(BOOKS_PER_PAGE) != 0) totalCount / BOOKS_PER_PAGE + 1
            else totalCount / BOOKS_PER_PAGE
        val nextPage = getNextPage(initialUrl)

        callback.onResult(result.works, 0, totalCount, null, nextPage)
    }

    override fun loadAfter(params: LoadParams<URL>, callback: LoadCallback<URL, Work>) {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<URL>, callback: LoadCallback<URL, Work>) {
        /*
        * There is no need to load previous nextPage because it's already loaded
        * */
    }

    private fun loadNParse(url: URL): Results {
        val xml = url.readText()
        val parser = GoodreadsParser()
        return parser.parse(xml)
    }

    /*
    * Builds initialUrl for next nextPage from current initialUrl
    * */
    private fun getNextPage(url: URL): URL? {
        val next = nextPage
        return if (next < totalPages) nextPageQuery(url, next) else null
    }
}