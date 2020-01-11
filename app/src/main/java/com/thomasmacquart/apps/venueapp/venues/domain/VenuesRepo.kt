package com.thomasmacquart.apps.venueapp.venues.domain

import com.thomasmacquart.apps.venueapp.core.AsyncResponse
import com.thomasmacquart.apps.venueapp.core.BaseRepository
import com.thomasmacquart.apps.venueapp.core.extensions.exhaustive
import com.thomasmacquart.apps.venueapp.venues.data.api.VenueDetailsApi
import com.thomasmacquart.apps.venueapp.venues.data.api.VenuesSearchApi
import com.thomasmacquart.apps.venueapp.venues.data.entities.MapBounds
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import com.thomasmacquart.apps.venueapp.venues.data.VenuesCache
import com.thomasmacquart.apps.venueapp.venues.data.entities.LatitudeLongitude
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class VenuesRepo @Inject constructor(
    private val api: VenuesSearchApi,
    private val cache : VenuesCache,
    private val detailsApi : VenueDetailsApi
) : BaseRepository() {

    private val foodCategory = "4d4b7105d754a06374d81259"

    fun loadVenues(target: LatitudeLongitude, bounds: MapBounds): Flow<AsyncResponse<List<Venue>>> =
        flow {
            emit(AsyncResponse.Success(cache.getVenues(bounds)))

            val ll = "${target.latitude},${target.longitude}"
            val swBounds = "${bounds.southWest.latitude},${bounds.southWest.longitude}"
            val neBounds = "${bounds.northEast.latitude},${bounds.northEast.longitude}"

            val result = safeApiResult { api.searchVenues(ll, swBounds, neBounds, foodCategory) }
            when (result) {
                is AsyncResponse.Success -> {
                    cache.updateCache(result.data.venues)
                    emit(AsyncResponse.Success(result.data.venues))
                }
                is AsyncResponse.Failed -> emit(AsyncResponse.Failed(IOException("Api call failed")))
            }.exhaustive


        }

    suspend fun loadVenueDetails(venueId : String) : AsyncResponse<Venue> {

        val result =  safeApiResult {detailsApi.getVenueDetails(venueId)}
        return when (result) {
            is AsyncResponse.Success -> AsyncResponse.Success(result.data.venue)
            is AsyncResponse.Failed -> AsyncResponse.Failed(IOException("Api call failed"))
        }
    }
}