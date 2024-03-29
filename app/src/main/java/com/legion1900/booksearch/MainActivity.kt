package com.legion1900.booksearch

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.legion1900.booksearch.databinding.ActivityMainBinding
import com.legion1900.booksearch.paging.BookAdapter
import com.legion1900.booksearch.parser.Results
import com.legion1900.booksearch.parser.Work
import com.legion1900.booksearch.utilities.ConnectionMonitor

import com.legion1900.booksearch.viewmodels.SearchViewModel
import com.legion1900.booksearch.utilities.hideKeyboard
import com.legion1900.booksearch.viewmodels.SearchViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

private const val MSG_NO_CONNECTION = "No connection"

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: SearchViewModel

    private lateinit var rvAdapter: BookAdapter

    private lateinit var rvLayoutManager: LinearLayoutManager

    private lateinit var connectionMonitor: ConnectionMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        connectionMonitor = ConnectionMonitor(this)

        initRecyclerView()
        initViewModel()

        binding.buttonSearch.setOnClickListener(::onSearchButtonClick)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    /*
    * UI preparation before executing query:
    * hide keyboard;
    * display loading animation;
    * */
    private fun prepareUi() = with(binding) {
        etQuery.hideKeyboard()
        loadingBar.visibility = View.VISIBLE
        rvResult.visibility = View.GONE
    }

    /*
    * RecyclerView preparation:
    * attach layout manager;
    * attach adapter;
    * */
    private fun initRecyclerView() {
        val noData = mutableListOf<Work>()
        rvAdapter = BookAdapter(noData)
        rvLayoutManager = LinearLayoutManager(this)
        binding.rvResult.let {
            it.layoutManager = rvLayoutManager
            it.adapter = rvAdapter
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
            .of(this, SearchViewModelFactory(this))
            .get(SearchViewModel::class.java)
        viewModel.queryResult.observe(this,
            Observer<Results> {
                rvAdapter.swapData(it.works)
                with(binding) {
                    loadingBar.visibility = View.GONE
                    rvResult.visibility = View.VISIBLE
                }
            })
    }

    /*
    * Search button click listener
    * */
    private fun onSearchButtonClick(view: View) {
        if (!connectionMonitor.isConnected) {
            Snackbar.make(binding.coordinator, MSG_NO_CONNECTION, Snackbar.LENGTH_LONG).show()
            return
        }
        prepareUi()
        viewModel.queryNew(binding.etQuery.text.toString())
        rvLayoutManager.scrollToPosition(0)
    }
}
