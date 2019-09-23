package com.legion1900.booksearch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.legion1900.booksearch.networking.NetworkApi
import com.legion1900.booksearch.parser.GoodreadsParser
import com.legion1900.booksearch.parser.Results
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SearchViewModel(
    val scope: CoroutineScope
) : ViewModel() {

    val queryResult = MutableLiveData<Results>()

    val client = NetworkApi()

    val parser = GoodreadsParser()

    // TODO: new DataSource should be queried here
    fun queryNew(query: String) {
        scope.launch {
            val xml = client.executeSearch(query)
            val result = parser.parse(xml)
            queryResult.value = result
        }
    }

    // TODO: add queryUpdate(URL) to extend list of data
    // TODO: move all download & parsing logic to my DataSource
    // TODO: get rid of AsyncTask in favour of coroutines
}
