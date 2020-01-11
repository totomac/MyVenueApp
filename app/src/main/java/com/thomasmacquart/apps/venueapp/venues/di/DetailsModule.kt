package com.thomasmacquart.apps.venueapp.venues.di

import com.thomasmacquart.apps.venueapp.venues.ui.viewmodel.DetailsViewModel
import com.thomasmacquart.apps.venueapp.venues.domain.VenuesRepo
import dagger.Module
import dagger.Provides

@Module
class DetailsModule {

    @Provides
    fun provideDetailsViewModelFactory(repo: VenuesRepo) : DetailsViewModel.Factory =
        DetailsViewModel.Factory(repo)
}