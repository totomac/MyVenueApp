package com.thomasmacquart.apps.venueapp.venues.data

import com.thomasmacquart.apps.venueapp.venues.data.entities.MapBounds
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VenuesCache {

    private val venuesCache = mutableSetOf<Venue>()

    suspend fun updateCache(venues: List<Venue>) {
        withContext(Dispatchers.IO) {
            venues.forEach {
                venuesCache.add(it)
            }
        }
    }

    suspend fun getVenues(bounds : MapBounds): List<Venue> {

        return withContext(Dispatchers.IO) {
            venuesCache
                .filter { venue ->
                    venue.isWithinBounds(bounds)
                }
        }
    }
}