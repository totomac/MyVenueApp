package com.thomasmacquart.apps.venueapp.venues.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VenuesSearchApi {
    @GET("v2/venues/search")
    suspend fun searchVenues(@Query("ll") latlng : String,
                             @Query("sw") swBounds : String,
                             @Query("ne") neBounds : String,
                             @Query("categoryId") categoryId : String,
                             @Query("intent") intent : String) : Response<SearchResponse>
}