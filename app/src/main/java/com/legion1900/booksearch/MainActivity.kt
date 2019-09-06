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
import com.legion1900.booksearch.parser.Results
import com.legion1900.booksearch.parser.Work
import com.legion1900.booksearch.utilities.ConnectionMonitor

import com.legion1900.booksearch.utilities.XmlViewModel
import com.legion1900.booksearch.utilities.hideKeyboard
import com.legion1900.booksearch.utilities.buildQuery

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: XmlViewModel

    private lateinit var rvAdapter: BookAdapter

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
        binding.rvResult.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = rvAdapter
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(XmlViewModel::class.java)
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
//            TODO: add loading animation
        if (connectionMonitor.isConnected) {
            prepareUi()
            viewModel.queryNew(buildQuery(binding.etQuery.text.toString()))
        } else {
            Snackbar.make(binding.coordinator, "No connection", Snackbar.LENGTH_LONG).show()
        }
    }
}
