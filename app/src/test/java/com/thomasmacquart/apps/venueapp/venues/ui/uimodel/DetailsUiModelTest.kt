package com.thomasmacquart.apps.venueapp.venues.ui.uimodel

import android.content.Context
import com.nhaarman.mockitokotlin2.whenever
import com.thomasmacquart.apps.venueapp.R
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock

import org.mockito.MockitoAnnotations

class DetailsUiModelTest {

    @Mock
    private lateinit var venue: Venue

    @Mock
    private lateinit var context : Context

    private lateinit var uiModel: DetailsUiModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        uiModel = DetailsUiModel(venue)
    }

    @Nested
    @DisplayName("Test getName")
    inner class TestGetName {
        @Test
        fun `Given testName`() {
            whenever(venue.name).thenReturn("taco el loco")

            assertEquals("taco el loco", uiModel.getName())
        }
    }

    @Nested
    @DisplayName("Test getRating")
    inner class TestGetRating {
        @Test
        fun `Given rating is not null`() {
            whenever(venue.rating).thenReturn(5.0)

            assertEquals("5.0", uiModel.getRating(context))
        }

        @Test
        fun `Given rating is null`() {
            whenever(venue.rating).thenReturn(null)

            whenever(context.getString(R.string.details_rating_unavailable)).thenReturn("coucou")
            assertEquals("coucou", uiModel.getRating(context))
        }
    }
}