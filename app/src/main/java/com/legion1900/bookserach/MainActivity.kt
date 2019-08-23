package com.legion1900.bookserach

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.legion1900.booksearch.R
import com.legion1900.booksearch.databinding.ActivityMainBinding

import com.legion1900.booksearch.utilities.XmlViewModel
import com.legion1900.booksearch.utilities.hideKeyboard
import com.legion1900.bookserach.utilities.buildQuery
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: XmlViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(XmlViewModel::class.java)
        viewModel.queryResult.observe(this,
            Observer<List<String>> {
                val builder = StringBuilder()
                for (res in it)
                    builder.append(res)
                binding.tvQueryResult.text = builder.toString()
            })

        binding.buttonSearch.setOnClickListener {
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
}
