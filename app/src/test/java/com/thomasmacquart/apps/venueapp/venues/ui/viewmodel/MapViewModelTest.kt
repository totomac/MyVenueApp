package com.thomasmacquart.apps.venueapp.venues.ui.viewmodel

import com.nhaarman.mockitokotlin2.*
import com.thomasmacquart.apps.venueapp.core.AsyncResponse
import com.thomasmacquart.apps.venueapp.utils.CoroutineTestExtension
import com.thomasmacquart.apps.venueapp.utils.InstantExecutorExtension
import com.thomasmacquart.apps.venueapp.utils.LiveDataTestUtil
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import com.thomasmacquart.apps.venueapp.venues.domain.VenuesRepo
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.IOException

@Extensions(
    ExtendWith(InstantExecutorExtension::class),
    ExtendWith(CoroutineTestExtension::class)
)
internal class MapViewModelTest {

    @Mock
    private lateinit var repo: VenuesRepo

    private lateinit var viewModel: MapViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = MapViewModel(repo)
    }

    @Nested
    @DisplayName("test load venues")
    inner class TestLoadVenues {
        @Test
        fun `Given repo return succeed`() {
            runBlocking {
                whenever(repo.loadVenues(any(), any())).thenReturn(
                    flow {
                        emit(AsyncResponse.Success(listOf<Venue>(mock())))
                    }
                )

                viewModel.loadVenues(1.0, 1.0, mock())
                verify(repo, times(1)).loadVenues(any(), any())
                assertTrue(LiveDataTestUtil.getValue(viewModel.getUiObservable()) is MapViewState.UpdateMapState)
            }
        }

        @Test
        fun `Given repo returns failure`() {
            runBlocking {
                whenever(repo.loadVenues(any(), any())).thenReturn(
                    flow {
                        emit(AsyncResponse.Failed(IOException("oops")))
                    }
                )

                viewModel.loadVenues(1.0, 1.0, mock())
                verify(repo, times(1)).loadVenues(any(), any())
                assertTrue(LiveDataTestUtil.getValue(viewModel.getUiObservable()) is MapViewState.ErrorState)
            }
        }
    }
}