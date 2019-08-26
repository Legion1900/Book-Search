package com.legion1900.booksearch.utilities

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.legion1900.booksearch.parser.GoodreadsParser
import com.legion1900.booksearch.parser.Results
import java.net.URL

class XmlViewModel : ViewModel() {

    val queryResult = MutableLiveData<Results>()

    fun queryNew(query: URL) {
        val executor = QueryExecutor()
        executor.execute(query)
    }

    // TODO add queryUpdate(URL) to extend list of data

    private inner class QueryExecutor : AsyncTask<URL, Unit, Results>() {
        override fun doInBackground(vararg queries: URL?): Results {
            val xml = queries[0]?.readText()

            val parser = GoodreadsParser()
            return parser.parse(xml!!)
        }

        override fun onPostExecute(result: Results) {
            super.onPostExecute(result)

            queryResult.value = result
        }
    }
}
