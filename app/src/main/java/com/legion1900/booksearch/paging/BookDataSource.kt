package com.legion1900.booksearch.paging

import androidx.paging.PageKeyedDataSource
import com.legion1900.booksearch.parser.GoodreadsParser
import com.legion1900.booksearch.parser.Work
import java.net.URL

class BookDataSource(val url: URL) : PageKeyedDataSource<URL, Work>() {
    override fun loadInitial(
        params: LoadInitialParams<URL>,
        callback: LoadInitialCallback<URL, Work>
    ) {
        val xml = url.readText()
        val parser = GoodreadsParser()
        val result = parser.parse(xml)
        val totalCount = result.run { endIndex - startIndex + 1 }
        callback.onResult(result.works, 0, totalCount, null, )
        // TODO: build URL for querying next page
    }

    override fun loadAfter(params: LoadParams<URL>, callback: LoadCallback<URL, Work>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<URL>, callback: LoadCallback<URL, Work>) {
        /*
        * There is no need to load previous page because it's already loaded
        * */
    }
}