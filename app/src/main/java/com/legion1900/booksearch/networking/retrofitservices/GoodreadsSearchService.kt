package com.legion1900.booksearch.networking.retrofitservices

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

private const val RELATIVE_PATH = "search.xml"

interface GoodreadsSearchService {
    @GET(RELATIVE_PATH)
    fun getXmlResponse(@QueryMap query: Map<String, String>): Call<String>
}