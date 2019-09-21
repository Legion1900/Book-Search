package com.legion1900.booksearch.viewmodels

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.legion1900.booksearch.networking.NetworkApi
import com.legion1900.booksearch.parser.GoodreadsParser
import com.legion1900.booksearch.parser.Results
import com.legion1900.booksearch.utilities.ConnectionMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URL
import java.net.UnknownHostException

class SearchViewModel(
    connectionMonitor: ConnectionMonitor,
    val scope: CoroutineScope,
    val errCallback: () -> Unit
) : ViewModel() {

    val queryResult = MutableLiveData<Results>()

    val client = NetworkApi(connectionMonitor)

    val parser = GoodreadsParser()

    // TODO: new DataSource should be queried here
    fun queryNew(query: String) {
        scope.launch {
            try {
                val xml = client.executeSearch(query)
                val result = parser.parse(xml)
                Log.d("FUCK", result.toString())
                queryResult.value = result
            }
            catch (e: UnknownHostException) {
                errCallback()
            }
        }
    }

    // TODO: add queryUpdate(URL) to extend list of data
    // TODO: move all download & parsing logic to my DataSource
    // TODO: get rid of AsyncTask in favour of coroutines
}
