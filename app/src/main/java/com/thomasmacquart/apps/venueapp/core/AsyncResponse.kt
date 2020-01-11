package com.thomasmacquart.apps.venueapp.core

sealed class AsyncResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : AsyncResponse<T>()
    data class Failed(val exception: Exception) : AsyncResponse<Nothing>()
}