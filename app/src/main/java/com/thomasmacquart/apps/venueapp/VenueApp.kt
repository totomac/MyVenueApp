package com.thomasmacquart.apps.venueapp

import android.app.Application
import com.thomasmacquart.apps.venueapp.core.di.DaggerApplicationComponent

class VenueApp : Application() {

    val appComponent = DaggerApplicationComponent.create()
}