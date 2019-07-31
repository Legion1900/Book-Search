package com.legion1900.booksearch

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import java.net.URL
import kotlin.io.readText

class MainActivity : AppCompatActivity() {

    private lateinit var buttonSearch: ImageButton
    private lateinit var queryField: EditText
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonSearch = findViewById(R.id.button_search)
        queryField = findViewById(R.id.et_query)
        tvResult = findViewById(R.id.tv_query_result)

        buttonSearch.setOnClickListener {
            val task = ApiCallTask()
            task.execute(buildQuery(queryField.text.toString()))
        }
    }

    // TODO add querying via AsyncTaskLoader!

    private fun buildQuery(query: String): URL {
        val uri = Uri.Builder()
            .scheme("https")
            .authority("www.goodreads.com")
            .appendPath("search.xml")
            .appendQueryParameter("key", BuildConfig.ApiKey)
            .appendQueryParameter("q", query)
            .build()
        return URL(uri.toString())
    }

    private inner class ApiCallTask() : AsyncTask<URL, Unit, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            buttonSearch.visibility = View.GONE
            queryField.visibility = View.GONE
            queryField.hideKeyboard()
            tvResult.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: URL?): String? {
            val url = params[0]
            Log.d("URL test", url.toString())
            return url?.readText()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            tvResult.text = result
        }
    }

    private fun EditText.hideKeyboard() {
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(windowToken, 0)
    }
}
