package com.thomasmacquart.apps.venueapp.venues.data.entities

data class LatitudeLongitude(val latitude : Double, val longitude : Double) {
    fun formatForQuery() : String = "${latitude},${longitude}"
}