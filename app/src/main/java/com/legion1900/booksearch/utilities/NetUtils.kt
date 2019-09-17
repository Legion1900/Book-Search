package com.legion1900.booksearch.utilities

import android.content.Context
import android.net.*
import com.legion1900.booksearch.BuildConfig
import java.net.URL

private const val SCHEME = "https"
private const val AUTHORITY = "www.goodreads.com"
private const val PATH = "search.xml"
private const val PARAM_Q = "q"
private const val PARAM_KEY = "key"
private const val PARAM_PAGE = "page"


fun buildQuery(
    query: String,
    scheme: String = SCHEME,
    authority: String = AUTHORITY,
    path: String = PATH,
    page: Int = 1
    ): URL {
    val uri = Uri.Builder()
        .scheme(scheme)
        .authority(authority)
        .appendPath(path)
        .appendQueryParameter(PARAM_KEY, BuildConfig.apiKey)
        .appendQueryParameter(PARAM_Q, query)
        .appendQueryParameter(PARAM_PAGE, page.toString())
        .build()
    return URL(uri.toString())
}

class ConnectionMonitor(context: Context) : ConnectivityManager.NetworkCallback() {

    init {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(request, this)
    }

    var isConnected: Boolean = false
        private set

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        isConnected = true
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        isConnected = false
    }
}