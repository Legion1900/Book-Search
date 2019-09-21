package com.legion1900.booksearch.networking

import com.legion1900.booksearch.BuildConfig
import com.legion1900.booksearch.networking.retrofitservices.GoodreadsSearchService
import com.legion1900.booksearch.utilities.ConnectionMonitor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.net.UnknownHostException

private const val BASE_URL = "https://www.goodreads.com"
private const val PARAM_Q = "q"
private const val PARAM_KEY = "key"
private const val PARAM_PAGE = "page"

class NetworkApi(private val connectionMonitor: ConnectionMonitor) {
    private val client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    @Throws(IOException::class, UnknownHostException::class)
    fun executeSearch(query: String, page: Int = 0): String {
        if (!connectionMonitor.isConnected) throw UnknownHostException("No connection")
        val service = client.create(GoodreadsSearchService::class.java)
        val param = mapOf(
            PARAM_KEY to BuildConfig.apiKey,
            PARAM_Q to query,
            PARAM_PAGE to page.toString()
        )
        val response = service.getXmlResponse(param).execute()
        return if (response.isSuccessful) response.body()!!
        else throw IOException("Problems occurred")
    }
}