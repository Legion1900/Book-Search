package com.legion1900.booksearch.utilities

import android.net.Uri
import com.legion1900.booksearch.BuildConfig
import java.net.URL

private val SCHEME = "https"
private val AUTHORITY = "www.goodreads.com"
private val PATH = "search.xml"
private val PARAM_Q = "q"
private val PARAM_KEY = "key"
private val PARAM_PAGE = "p"


fun buildQuery(
    query: String,
    scheme: String = SCHEME,
    authority: String = AUTHORITY,
    path: String = PATH,
    page: Int = 0
    ): URL {
    val uri = Uri.Builder()
        .scheme(scheme)
        .authority(authority)
        .appendPath(path)
        .appendQueryParameter(PARAM_KEY, BuildConfig.ApiKey)
        .appendQueryParameter(PARAM_Q, query)
        .appendQueryParameter(PARAM_PAGE, page.toString())
        .build()
    return URL(uri.toString())
}