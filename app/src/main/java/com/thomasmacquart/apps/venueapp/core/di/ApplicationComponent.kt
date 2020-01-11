package com.thomasmacquart.apps.venueapp.core.di

import com.thomasmacquart.apps.venueapp.venues.di.DetailsModule
import com.thomasmacquart.apps.venueapp.venues.di.MapModule
import com.thomasmacquart.apps.venueapp.venues.di.VenueModule
import com.thomasmacquart.apps.venueapp.venues.ui.fragment.DetailsFragment
import com.thomasmacquart.apps.venueapp.venues.ui.fragment.MapFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, VenueModule::class, MapModule::class, DetailsModule::class])
interface ApplicationComponent {

    fun inject(fragment : MapFragment)

    fun inject(fragment: DetailsFragment)
}