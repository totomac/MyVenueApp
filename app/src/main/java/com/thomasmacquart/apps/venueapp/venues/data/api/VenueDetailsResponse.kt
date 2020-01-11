package com.thomasmacquart.apps.venueapp.venues.data.api

import com.squareup.moshi.Json
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue

data class VenueDetailsResponse (
    @Json(name = "venue")
    val venue : Venue
)