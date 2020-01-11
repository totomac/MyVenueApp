package com.thomasmacquart.apps.venueapp.core

import retrofit2.Response
import java.io.IOException

open class BaseRepository{

    suspend fun <T: Any> safeApiResult(call: suspend ()-> Response<T>) : AsyncResponse<T>{
        try {
            val response = call.invoke()
            if (response.isSuccessful) return AsyncResponse.Success(response.body()!!)

            return AsyncResponse.Failed(IOException("Error Occurred during getting safe Api result"))
        } catch (e : Exception) {
            return AsyncResponse.Failed(IOException("Error has occured"))
        }
    }
}