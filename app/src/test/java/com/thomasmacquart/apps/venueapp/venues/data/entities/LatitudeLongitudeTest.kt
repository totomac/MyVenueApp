package com.thomasmacquart.apps.venueapp.venues.data.entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class LatitudeLongitudeTest {

    private lateinit var latitudeLongitude: LatitudeLongitude

    @Test
    fun `Given latitude and longitude`() {
        latitudeLongitude = LatitudeLongitude(1.0, 2.0)

        assertEquals("1.0,2.0", latitudeLongitude.formatForQuery())
    }
}