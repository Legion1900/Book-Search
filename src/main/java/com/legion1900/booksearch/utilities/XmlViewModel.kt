package com.legion1900.booksearch.utilities

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.net.URL
import java.util.*

class XmlViewModel : ViewModel() {

    val queryResult = MutableLiveData<List<String>>()

    fun queryNew(query: URL) {
        val executor = QueryExecutor()
        executor.execute(query)
    }

    // TODO add queryUpdate(URL) to extend list of data

    private inner class QueryExecutor : AsyncTask<URL, Unit, String?>() {
        override fun doInBackground(vararg queries: URL?): String? = queries[0]?.readText()

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val parser = GoodreadsParser()
            val search = parser.parse(result!!)
            val show = "Results start: ${search.first}\nResults end: ${search.second}\nTotal results: ${search.third}"
//            queryResult.value = listOf(result)
            queryResult.value = listOf(show)
        }
    }
}
