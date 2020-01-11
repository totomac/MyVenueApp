package com.thomasmacquart.apps.venueapp.venues.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VenueDetailsApi {
    @GET("v2/venues/{venueId}")
    suspend fun getVenueDetails(@Path("venueId") venueId : String) : Response<VenueDetailsResponse>
}