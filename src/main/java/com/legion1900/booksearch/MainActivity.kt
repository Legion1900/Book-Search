package com.legion1900.booksearch

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.legion1900.booksearch.databinding.ActivityMainBinding
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.buttonSearch.setOnClickListener {
            val task = ApiCallTask()
            task.execute(buildQuery(binding.etQuery.text.toString()))
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
            binding.run {
                buttonSearch.visibility = View.GONE
                etQuery.visibility = View.GONE
                etQuery.hideKeyboard()
                svResult.visibility = View.VISIBLE
            }
        }

        override fun doInBackground(vararg params: URL?): String? {
            val url = params[0]
            return url?.readText()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            binding.tvQueryResult.text = result
        }
    }

    private fun EditText.hideKeyboard() {
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(windowToken, 0)
    }
}
