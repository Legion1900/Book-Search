package com.legion1900.booksearch.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.legion1900.booksearch.networking.NetworkApi
import com.legion1900.booksearch.parser.GoodreadsParser
import com.legion1900.booksearch.parser.Results
import com.legion1900.booksearch.utilities.ConnectionMonitor
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL

class SearchViewModel(connectionMonitor: ConnectionMonitor) : ViewModel() {

    val queryResult = MutableLiveData<Results>()

    val client = NetworkApi(connectionMonitor)

    val parser = GoodreadsParser()

    // TODO: new DataSource should be queried here
    fun queryNew(query: String) {
        val executor = QueryExecutor(
            WeakReference(this)
        )
        executor.execute(query)
    }

    // TODO: add queryUpdate(URL) to extend list of data
    // TODO: move all download & parsing logic to my DataSource
    // TODO: get rid of AsyncTask in favour of coroutines

    private class QueryExecutor(val viewModel: WeakReference<SearchViewModel>)
        : AsyncTask<String, Unit, Results>() {

        override fun doInBackground(vararg queries: String?): Results =
            with(viewModel.get()!!) {
                val result = client.executeSearch(queries[0]!!)
                parser.parse(result)
            }

        override fun onPostExecute(result: Results) {
            super.onPostExecute(result)
            viewModel.get()?.apply {
                queryResult.value = result
            }
        }
    }
}
