package com.legion1900.booksearch.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.legion1900.booksearch.parser.GoodreadsParser
import com.legion1900.booksearch.parser.Results
import java.lang.ref.WeakReference
import java.net.URL

class SearchViewModel : ViewModel() {

    val queryResult = MutableLiveData<Results>()

    fun queryNew(query: URL) {
        val executor = QueryExecutor(
            WeakReference(queryResult)
        )
        executor.execute(query)
    }

    // TODO add queryUpdate(URL) to extend list of data

    private class QueryExecutor(val liveData: WeakReference<MutableLiveData<Results>>)
        : AsyncTask<URL, Unit, Results>() {

        override fun doInBackground(vararg queries: URL?): Results {
            val xml = queries[0]?.readText()

            val parser = GoodreadsParser()
            return parser.parse(xml!!)
        }

        override fun onPostExecute(result: Results) {
            super.onPostExecute(result)

            liveData.get()?.value = result
        }
    }
}
