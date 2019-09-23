package com.legion1900.booksearch.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.legion1900.booksearch.utilities.ConnectionMonitor
import kotlinx.coroutines.CoroutineScope

class SearchViewModelFactory(
    private val scope: CoroutineScope
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(scope) as T
    }
}