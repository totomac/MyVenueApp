package com.thomasmacquart.apps.venueapp.venues.ui.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.thomasmacquart.apps.venueapp.core.utils.AsyncResponse
import com.thomasmacquart.apps.venueapp.core.extensions.exhaustive
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import com.thomasmacquart.apps.venueapp.venues.domain.VenuesRepo
import com.thomasmacquart.apps.venueapp.venues.ui.uimodel.DetailsUiModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val repo: VenuesRepo
) : ViewModel() {

    private val _uiObservable : MutableLiveData<DetailsViewState> = MutableLiveData()
    fun getUiObservable() : LiveData<DetailsViewState> = _uiObservable

    fun loadVenueDetails(venueId : String) {
        _uiObservable.value = DetailsViewState.OnLoading
        viewModelScope.launch {
            val result = repo.loadVenueDetails(venueId)
            when (result) {
                is AsyncResponse.Success -> onSuccess(result.data)
                is AsyncResponse.Failed -> onError(result.exception.message.toString())
            }.exhaustive
        }
    }

    private fun onSuccess(venue: Venue) {
        _uiObservable.value =
            DetailsViewState.OnPopulateData(
                DetailsUiModel(venue)
            )
    }

    private fun onError(error: String) {
        _uiObservable.value = DetailsViewState.OnError(error)
    }

    class Factory(
        private val repo: VenuesRepo
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            DetailsViewModel(repo) as T

    }


    companion object {

        fun obtain(scope: FragmentActivity, factory: Factory): DetailsViewModel =
            ViewModelProviders.of(scope, factory)[DetailsViewModel::class.java]
    }
}

sealed class DetailsViewState {
    data class OnPopulateData(val uiModel : DetailsUiModel) : DetailsViewState()
    data class OnError(val  error : String) : DetailsViewState()
    object OnLoading : DetailsViewState()
}