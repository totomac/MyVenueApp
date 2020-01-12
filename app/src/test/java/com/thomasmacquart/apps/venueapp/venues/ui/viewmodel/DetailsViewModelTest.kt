package com.thomasmacquart.apps.venueapp.venues.ui.viewmodel

import com.nhaarman.mockitokotlin2.*
import com.thomasmacquart.apps.venueapp.core.utils.AsyncResponse
import com.thomasmacquart.apps.venueapp.utils.CoroutineTestExtension
import com.thomasmacquart.apps.venueapp.utils.InstantExecutorExtension
import com.thomasmacquart.apps.venueapp.utils.LiveDataTestUtil
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import com.thomasmacquart.apps.venueapp.venues.domain.VenuesRepo
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@Extensions(
    ExtendWith(InstantExecutorExtension::class),
    ExtendWith(CoroutineTestExtension::class)
)
internal class DetailsViewModelTest {
    @Mock
    private lateinit var repo: VenuesRepo

    private lateinit var viewModel: DetailsViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = DetailsViewModel(repo)
    }

    @Nested
    @DisplayName("test load venues")
    inner class TestLoadVenueDetails {

        @Test
        fun `Given repo succeed`() {
            runBlocking {
                val result = AsyncResponse.Success(mock<Venue>())
                whenever(repo.loadVenueDetails(any())).thenReturn(result)

                viewModel.loadVenueDetails("1")
                verify(repo, times(1)).loadVenueDetails(any())

                assertTrue(LiveDataTestUtil.getValue(viewModel.getUiObservable()) is DetailsViewState.OnPopulateData)
            }
        }

        @Test
        fun `Given repo fails`() {
            runBlocking {
                val result = AsyncResponse.Failed(mock())
                whenever(repo.loadVenueDetails(any())).thenReturn(result)

                viewModel.loadVenueDetails("1")
                verify(repo, times(1)).loadVenueDetails(any())

                assertTrue(LiveDataTestUtil.getValue(viewModel.getUiObservable()) is DetailsViewState.OnError)
            }
        }
    }
}