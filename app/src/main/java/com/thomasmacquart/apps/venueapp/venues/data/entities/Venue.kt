package com.thomasmacquart.apps.venueapp.venues.data.entities

import com.squareup.moshi.Json

data class Venue (
    @Json(name = "id")
    val id : String,
    @Json(name = "name")
    val name : String,
    @Json(name = "location")
    val location : Location,
    @Json(name = "rating")
    val rating : Double?
) {
    fun isWithinBounds(bounds : MapBounds) :Boolean {
        return isWithinLongitudes(bounds.southWest.longitude, bounds.northEast.longitude)
                && isWithinLatitudes(bounds.southWest.latitude, bounds.northEast.latitude)
    }

    private fun isWithinLongitudes(westLng : Double, eastLng : Double) : Boolean{
        return location.longitude > westLng && location.longitude < eastLng
    }

    private fun isWithinLatitudes(southLat : Double, northLat : Double) : Boolean{
        return location.latitude > southLat && location.latitude < northLat
    }
}

data class Location(
    @Json(name = "lat")
    val latitude : Double,
    @Json(name = "lng")
    val longitude : Double
)