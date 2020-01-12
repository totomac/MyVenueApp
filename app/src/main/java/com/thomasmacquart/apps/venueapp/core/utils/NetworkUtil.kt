package com.thomasmacquart.apps.venueapp.core.utils

import retrofit2.Response
import java.io.IOException

suspend fun <T : Any> safeApiCall(errorMessage: String, call: suspend () -> Response<T>): AsyncResponse<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) return AsyncResponse.Success(response.body()!!)

        return AsyncResponse.Failed(IOException(errorMessage))
    } catch (e: Exception) {
        // An exception was thrown when calling the API so we're converting this to an IOException
        AsyncResponse.Failed(IOException(errorMessage, e))
    }
}