package com.thomasmacquart.apps.venueapp.venues.ui.uimodel

import android.content.Context
import com.thomasmacquart.apps.venueapp.R
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue

class DetailsUiModel(private val venue: Venue) {

    fun getName() : String = venue.name

    fun getRating(context: Context) : String {
        return venue.rating?.let {
            it.toString()
        } ?: context.getString(R.string.details_rating_unavailable)
    }
}