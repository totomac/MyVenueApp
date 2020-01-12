package com.thomasmacquart.apps.venueapp.venues.domain

import com.thomasmacquart.apps.venueapp.core.utils.AsyncResponse
import com.thomasmacquart.apps.venueapp.core.extensions.exhaustive
import com.thomasmacquart.apps.venueapp.core.utils.safeApiCall
import com.thomasmacquart.apps.venueapp.venues.data.api.VenueDetailsApi
import com.thomasmacquart.apps.venueapp.venues.data.api.VenuesSearchApi
import com.thomasmacquart.apps.venueapp.venues.data.entities.MapBounds
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import com.thomasmacquart.apps.venueapp.venues.data.VenuesCache
import com.thomasmacquart.apps.venueapp.venues.data.entities.LatitudeLongitude
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VenuesRepo @Inject constructor(
    private val api: VenuesSearchApi,
    private val cache: VenuesCache,
    private val detailsApi: VenueDetailsApi
) {

    private val FOOD_CATEGORY = "4d4b7105d754a06374d81259"
    private val INTENT = "browse"

    /**
     * We want to display venues quickly.
     * First we emit the data from cache, then we retrieve venues from network update cache and emit
     */
    fun loadVenues(target: LatitudeLongitude, bounds: MapBounds): Flow<AsyncResponse<List<Venue>>> =
        flow {
            emit(AsyncResponse.Success(cache.getVenues(bounds)))

            val ll = target.formatForQuery()
            val swBounds = bounds.southWest.formatForQuery()
            val neBounds = bounds.northEast.formatForQuery()

            val result = safeApiCall(
                "loadVenues failed"
            ) { api.searchVenues(ll, swBounds, neBounds, FOOD_CATEGORY, INTENT) }

            when (result) {
                is AsyncResponse.Success -> {
                    cache.updateCache(result.data.venues)
                    emit(AsyncResponse.Success(result.data.venues))
                }
                is AsyncResponse.Failed -> emit(AsyncResponse.Failed(result.exception))
            }.exhaustive


        }

    suspend fun loadVenueDetails(venueId: String): AsyncResponse<Venue> {

        val result =
            safeApiCall("Load venue details failed") { detailsApi.getVenueDetails(venueId) }
        return when (result) {
            is AsyncResponse.Success -> AsyncResponse.Success(result.data.venue)
            is AsyncResponse.Failed -> AsyncResponse.Failed(result.exception)
        }
    }
}