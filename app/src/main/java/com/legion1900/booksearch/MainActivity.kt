package com.legion1900.booksearch

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.legion1900.booksearch.databinding.ActivityMainBinding
import com.legion1900.booksearch.parser.Results
import com.legion1900.booksearch.parser.Work

import com.legion1900.booksearch.utilities.XmlViewModel
import com.legion1900.booksearch.utilities.hideKeyboard
import com.legion1900.booksearch.utilities.buildQuery

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: XmlViewModel

    private lateinit var viewAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initRecyclerView()

        viewModel = ViewModelProviders.of(this).get(XmlViewModel::class.java)
        viewModel.queryResult.observe(this,
            Observer<Results> {
                viewAdapter.swapData(it.works)
            })

        binding.buttonSearch.setOnClickListener {
            //            TODO: add connection check
//            TODO: add loading animation
            prepareUi()
            viewModel.queryNew(buildQuery(binding.etQuery.text.toString()))
        }
    }

    /*
    * UI preparation before executing query
    * */
    private fun prepareUi() {
        binding.run {
            etQuery.hideKeyboard()
        }
    }

    private fun initRecyclerView() {
        val noData = mutableListOf<Work>()
        viewAdapter = BookAdapter(noData)
        binding.rvResult.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = viewAdapter
        }
    }
}
