package com.thomasmacquart.apps.venueapp.venues.di

import com.thomasmacquart.apps.venueapp.venues.ui.viewmodel.MapViewModel
import com.thomasmacquart.apps.venueapp.venues.domain.VenuesRepo
import dagger.Module
import dagger.Provides

@Module
class MapModule {

    @Provides
    fun provideMapViewModelFactory(repo: VenuesRepo) : MapViewModel.Factory =
        MapViewModel.Factory(repo)
}