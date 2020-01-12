package com.thomasmacquart.apps.venueapp.venues.domain

import com.nhaarman.mockitokotlin2.*
import com.thomasmacquart.apps.venueapp.core.utils.AsyncResponse
import com.thomasmacquart.apps.venueapp.utils.CoroutineTestExtension
import com.thomasmacquart.apps.venueapp.utils.InstantExecutorExtension
import com.thomasmacquart.apps.venueapp.venues.data.VenuesCache
import com.thomasmacquart.apps.venueapp.venues.data.api.SearchResponse
import com.thomasmacquart.apps.venueapp.venues.data.api.VenueDetailsApi
import com.thomasmacquart.apps.venueapp.venues.data.api.VenueDetailsResponse
import com.thomasmacquart.apps.venueapp.venues.data.api.VenuesSearchApi
import com.thomasmacquart.apps.venueapp.venues.data.entities.LatitudeLongitude
import com.thomasmacquart.apps.venueapp.venues.data.entities.MapBounds
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

@Extensions(
    ExtendWith(InstantExecutorExtension::class),
    ExtendWith(CoroutineTestExtension::class)
)
internal class VenuesRepoTest {

    @Mock
    private lateinit var searchApi: VenuesSearchApi

    @Mock
    private lateinit var detailsApi: VenueDetailsApi

    @Mock
    private lateinit var cache: VenuesCache

    @Mock
    private lateinit var bounds: MapBounds
    @Mock
    private lateinit var swLatLng: LatitudeLongitude
    @Mock
    private lateinit var neLatLng: LatitudeLongitude

    private lateinit var repo: VenuesRepo

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(bounds.northEast).thenReturn(neLatLng)
        whenever(bounds.southWest).thenReturn(swLatLng)
        whenever(neLatLng.formatForQuery()).thenReturn("1.0,1.0")
        whenever(swLatLng.formatForQuery()).thenReturn("1.0,1.0")

        repo = VenuesRepo(searchApi, cache, detailsApi)
    }

    @Nested
    @DisplayName("test load venues")
    inner class TestLoadVenues {

        @Test
        fun `Given cache is empty and api succeed`() {
            runBlocking {
                val searchResponse = mock<SearchResponse>()
                val venue = mock<Venue>()
                whenever(venue.id).thenReturn("1")

                whenever(searchResponse.venues).thenReturn(listOf(venue))
                whenever(cache.getVenues(any())).thenReturn(emptyList())

                val response = mock<Response<SearchResponse>>()
                whenever(response.isSuccessful).thenReturn(true)
                whenever(response.body()).thenReturn(searchResponse)

                whenever(searchApi.searchVenues(any(), any(), any(), any(), any())).thenReturn(
                    response
                )

                val result = mutableListOf<AsyncResponse<List<Venue>>>()
                repo.loadVenues(LatitudeLongitude(1.0, 1.0), bounds).collect {
                    result.addAll(listOf(it))
                }

                verify(searchApi, times((1))).searchVenues(any(), any(), any(), any(), any())
                verify(cache, times(1)).getVenues(any())
                verify(cache, times(1)).updateCache(any())

                assertEquals(0, (result[0] as AsyncResponse.Success).data.size)
                assertEquals("1", (result[1] as AsyncResponse.Success).data.first().id)

            }
        }

        @Test
        fun `Given cache is not empty and api succeed`() {
            runBlocking {
                val searchResponse = mock<SearchResponse>()
                val venue = mock<Venue>()
                whenever(venue.id).thenReturn("1")

                val cachedVenue = mock<Venue>()
                whenever(cachedVenue.id).thenReturn("2")

                whenever(searchResponse.venues).thenReturn(listOf(venue))
                whenever(cache.getVenues(any())).thenReturn(listOf(cachedVenue))

                val response = mock<Response<SearchResponse>>()
                whenever(response.isSuccessful).thenReturn(true)
                whenever(response.body()).thenReturn(searchResponse)

                whenever(searchApi.searchVenues(any(), any(), any(), any(), any())).thenReturn(
                    response
                )

                val result = mutableListOf<AsyncResponse<List<Venue>>>()
                repo.loadVenues(LatitudeLongitude(1.0, 1.0), bounds).collect {
                    result.addAll(listOf(it))
                }

                verify(searchApi, times((1))).searchVenues(any(), any(), any(), any(), any())
                verify(cache, times(1)).getVenues(any())
                verify(cache, times(1)).updateCache(any())

                assertEquals("2", (result[0] as AsyncResponse.Success).data.first().id)
                assertEquals("1", (result[1] as AsyncResponse.Success).data.first().id)

            }
        }
    }

    @Nested
    @DisplayName("Test load venue details")
    inner class TestLoadVenueDetails {
        @Test
        fun `Given api call succeed`() {
            runBlocking {
                val venueDetailsResponse = mock<VenueDetailsResponse>()
                val venue = mock<Venue>()
                whenever(venue.id).thenReturn("1")
                whenever(venueDetailsResponse.venue).thenReturn(venue)

                val response = mock<Response<VenueDetailsResponse>>()
                whenever(response.isSuccessful).thenReturn(true)
                whenever(response.body()).thenReturn(venueDetailsResponse)

                whenever(detailsApi.getVenueDetails(any())).thenReturn(response)

                val result = repo.loadVenueDetails("1")
                verify(detailsApi, times(1)).getVenueDetails(any())

                assertEquals("1", (result as AsyncResponse.Success).data.id)
            }
        }

        @Test
        fun `Given api call fails`() {
            runBlocking {
                val venueDetailsResponse = mock<VenueDetailsResponse>()
                val venue = mock<Venue>()
                whenever(venue.id).thenReturn("1")
                whenever(venueDetailsResponse.venue).thenReturn(venue)

                val response = mock<Response<VenueDetailsResponse>>()
                whenever(response.isSuccessful).thenReturn(false)
                whenever(response.body()).thenReturn(venueDetailsResponse)

                whenever(detailsApi.getVenueDetails(any())).thenReturn(response)

                val result = repo.loadVenueDetails("1")
                verify(detailsApi, times(1)).getVenueDetails(any())

                assertTrue(result is AsyncResponse.Failed)
            }
        }
    }

}