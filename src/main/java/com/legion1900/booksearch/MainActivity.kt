package com.legion1900.booksearch

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.legion1900.booksearch.databinding.ActivityMainBinding
import com.legion1900.booksearch.utilities.QueryExecutor
import com.legion1900.booksearch.utilities.buildQuery
import com.legion1900.booksearch.utilities.hideKeyboard

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {

    private val LOADER_ID = 1

    private lateinit var binding: ActivityMainBinding

    private lateinit var loaderManager: LoaderManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        loaderManager = LoaderManager.getInstance(this)

        binding.buttonSearch.setOnClickListener {
            prepareUi()
            loaderManager.restartLoader(LOADER_ID, null, this)
        }
    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<String> {
        return QueryExecutor(this, buildQuery(binding.etQuery.text.toString()))
    }

    override fun onLoadFinished(p0: Loader<String>, p1: String?) {
        binding.run {
            tvQueryResult.text = p1
        }
    }

    override fun onLoaderReset(p0: Loader<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*
    * UI preparation before executing query
    * */
    private fun prepareUi() {
        binding.run {
            etQuery.hideKeyboard()
        }
    }
}
