package com.thomasmacquart.apps.venueapp.venues.data

import com.nhaarman.mockitokotlin2.whenever
import com.thomasmacquart.apps.venueapp.venues.data.entities.LatitudeLongitude
import com.thomasmacquart.apps.venueapp.venues.data.entities.Location
import com.thomasmacquart.apps.venueapp.venues.data.entities.MapBounds
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class VenueTest {

    @Nested
    @DisplayName("Test isWithinBounds")
    inner class testIsWithinBounds {

        @Mock
        private lateinit var location: Location

        private lateinit var venue: Venue

        @BeforeEach
        fun setUp() {
            MockitoAnnotations.initMocks(this)

            venue = Venue(
                "1",
                "test",
                location,
                2.0
            )
        }

        @Test
        fun `Given west and east longitudes are equals`() {
            whenever(location.latitude).thenReturn(2.0)
            whenever(location.longitude).thenReturn(2.0)

            val bounds = MapBounds(
                LatitudeLongitude(
                    30.0,
                    2.0
                ),
                LatitudeLongitude(20.0, 2.0)
            )
            assertFalse(
                venue.isWithinBounds(bounds)
            )
        }

        @Test
        fun `Given south and north longitudes are equals`() {
            whenever(location.latitude).thenReturn(2.0)
            whenever(location.longitude).thenReturn(2.0)

            val bounds = MapBounds(
                LatitudeLongitude(
                    30.0,
                    2.0
                ),
                LatitudeLongitude(20.0, 2.0)
            )
            assertFalse(
                venue.isWithinBounds(bounds)
            )
        }

        @Test
        fun `Given venue is in the bound`() {
            whenever(location.latitude).thenReturn(2.0)
            whenever(location.longitude).thenReturn(-1.5)

            val bounds = MapBounds(
                LatitudeLongitude(
                    -30.0,
                    -2.0
                ),
                LatitudeLongitude(20.0, -1.0)
            )
            assertTrue(
                venue.isWithinBounds(bounds)
            )
        }

        @Test
        fun `Given venue is outside bounds by longitude`() {
            whenever(location.latitude).thenReturn(2.0)
            whenever(location.longitude).thenReturn(4.0)

            val bounds = MapBounds(
                LatitudeLongitude(
                    -30.0,
                    -2.0
                ),
                LatitudeLongitude(20.0, -1.0)
            )
            assertFalse(
                venue.isWithinBounds(bounds)
            )
        }

        @Test
        fun `Given venue is outside bounds by latitude`() {
            whenever(location.latitude).thenReturn(90.0)
            whenever(location.longitude).thenReturn(-1.5)

            val bounds = MapBounds(
                LatitudeLongitude(
                    -30.0,
                    -2.0
                ),
                LatitudeLongitude(20.0, -1.0)
            )
            assertFalse(venue.isWithinBounds(bounds))
        }
    }
}