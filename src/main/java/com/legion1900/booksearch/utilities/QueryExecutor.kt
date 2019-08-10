package com.legion1900.booksearch.utilities

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import android.util.Log
import java.net.URL

class QueryExecutor(context: Context, val query: URL) : AsyncTaskLoader<String>(context) {

    init {
        Log.d("Testing", "New object created!")
    }

    var data: String? = null

    /*
    * It`s like onPreExecute in AsyncTask.
    * forceLoad() call is mandatory!!
    * */
    override fun onStartLoading() {
            forceLoad()
    }

    /*
    * It`s like doInBackground in AsyncTask
    * */
    override fun loadInBackground(): String? {
        return query.readText()
    }
}