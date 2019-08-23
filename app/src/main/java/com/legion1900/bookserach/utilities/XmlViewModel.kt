package com.legion1900.booksearch.utilities

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.legion1900.booksearch.parser.GoodreadsParser
import java.lang.StringBuilder
import java.net.URL

class XmlViewModel : ViewModel() {

    val queryResult = MutableLiveData<List<String>>()

    fun queryNew(query: URL) {
        val executor = QueryExecutor()
        executor.execute(query)
    }

    // TODO add queryUpdate(URL) to extend list of data

    private inner class QueryExecutor : AsyncTask<URL, Unit, MutableList<String>>() {
        override fun doInBackground(vararg queries: URL?): MutableList<String> {
            val xml = queries[0]?.readText()

            val parser = GoodreadsParser()
            val results = parser.parse(xml!!)
            val toPost = mutableListOf<String>()
            val builder = StringBuilder()
            var cnt = 1
            for (work in results.works) {
                builder.append("${cnt++}.\n")
                builder.append(work.author.name)
                builder.append("\n")
                builder.append(work.title)
                builder.append("\n")
                builder.append(work.avgRating)
                builder.append("\n")
                builder.append("___________\n")

                toPost.add(builder.toString())
                builder.clear()
            }
            return toPost
        }

        override fun onPostExecute(result: MutableList<String>) {
            super.onPostExecute(result)

            queryResult.value = result

//            queryResult.value = listOf(show)
        }
    }
}
