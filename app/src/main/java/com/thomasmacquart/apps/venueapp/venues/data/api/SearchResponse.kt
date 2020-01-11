package com.thomasmacquart.apps.venueapp.venues.data.api

import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue

data class SearchResponse (
    val venues :List<Venue>
)