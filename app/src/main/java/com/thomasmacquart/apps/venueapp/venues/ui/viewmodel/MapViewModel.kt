package com.thomasmacquart.apps.venueapp.venues.ui.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.thomasmacquart.apps.venueapp.core.utils.SingleLiveEvent
import com.thomasmacquart.apps.venueapp.core.AsyncResponse
import com.thomasmacquart.apps.venueapp.core.extensions.exhaustive
import com.thomasmacquart.apps.venueapp.venues.data.entities.LatitudeLongitude
import com.thomasmacquart.apps.venueapp.venues.data.entities.MapBounds
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import com.thomasmacquart.apps.venueapp.venues.domain.VenuesRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(private val repo : VenuesRepo) : ViewModel() {

    /**
     * [_uiObservable] ensure that markers are set only once during lifecycle of the fragment
     */
    private val _uiObservable =
        SingleLiveEvent<MapViewState>()
    fun getUiObservable() : SingleLiveEvent<MapViewState> = _uiObservable

    fun loadVenues(lat : Double, lng : Double, bounds : MapBounds) {
        viewModelScope.launch {
            repo.loadVenues(LatitudeLongitude(lat, lng), bounds).collect{ result ->
                when (result) {
                    is AsyncResponse.Success -> onSuccess(result.data)
                    is AsyncResponse.Failed -> onError(result.exception.message.toString())
                }.exhaustive
            }
        }
    }

    private fun onSuccess(venues: List<Venue>) {
        _uiObservable.value =
            MapViewState.UpdateMapState(
                venues
            )
    }

    private fun onError(error : String) {
        _uiObservable.value = MapViewState.ErrorState(error)
    }

    class Factory(
        private val repo: VenuesRepo
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            MapViewModel(repo) as T

    }

    companion object {

        fun obtain(scope: FragmentActivity, factory: Factory): MapViewModel =
            ViewModelProviders.of(scope, factory)[MapViewModel::class.java]
    }
}

sealed class MapViewState {
    data class UpdateMapState(val venues : List<Venue>) : MapViewState()
    data class ErrorState(val error : String) : MapViewState()
}