package com.thomasmacquart.apps.venueapp.venues.di

import com.thomasmacquart.apps.venueapp.venues.data.api.VenueDetailsApi
import com.thomasmacquart.apps.venueapp.venues.data.api.VenuesSearchApi
import com.thomasmacquart.apps.venueapp.venues.data.VenuesCache
import com.thomasmacquart.apps.venueapp.venues.domain.VenuesRepo
import com.thomasmacquart.apps.venueapp.venues.ui.viewmodel.DetailsViewModel
import com.thomasmacquart.apps.venueapp.venues.ui.viewmodel.MapViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VenueModule {

    @Provides
    fun provideVenueCache(): VenuesCache = VenuesCache()

    @Singleton
    @Provides
    fun provideVenueRepo(
        api: VenuesSearchApi,
        cache: VenuesCache,
        detailsApi: VenueDetailsApi
    ): VenuesRepo =
        VenuesRepo(api, cache, detailsApi)

    @Provides
    fun provideDetailsViewModelFactory(repo: VenuesRepo) : DetailsViewModel.Factory =
        DetailsViewModel.Factory(repo)

    @Provides
    fun provideMapViewModelFactory(repo: VenuesRepo) : MapViewModel.Factory =
        MapViewModel.Factory(repo)
}