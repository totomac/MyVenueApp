package com.thomasmacquart.apps.venueapp.venues.di

import com.thomasmacquart.apps.venueapp.venues.data.api.VenueDetailsApi
import com.thomasmacquart.apps.venueapp.venues.data.api.VenuesSearchApi
import com.thomasmacquart.apps.venueapp.venues.data.VenuesCache
import com.thomasmacquart.apps.venueapp.venues.domain.VenuesRepo
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
}