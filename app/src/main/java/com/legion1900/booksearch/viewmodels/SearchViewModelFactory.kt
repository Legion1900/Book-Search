package com.legion1900.booksearch.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.legion1900.booksearch.utilities.ConnectionMonitor

class SearchViewModelFactory(
    private val param: ConnectionMonitor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(param) as T
    }
}