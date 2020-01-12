package com.thomasmacquart.apps.venueapp.core.utils

import okhttp3.Interceptor
import okhttp3.Response

class CredentialsInterceptor : Interceptor {

    private val CLIENT_ID_VALUE = "W2FK0CRCFXSIDJ3IN4WTS5DJLS0DQPHT3UNGORS4OJHKSBLV"
    private val CLIENT_SECRET_VALUE = "VFZ5DVXPZDCGDGQWPGTOEOKUU4XOYGTO4A5QMMJ3EM1WYGQR"

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val url = oldRequest.url.newBuilder()
            .addQueryParameter("client_id", CLIENT_ID_VALUE)
            .addQueryParameter("client_secret", CLIENT_SECRET_VALUE)
            .addQueryParameter("v", "20190425")
            .build()
        val newRequest = oldRequest.newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }
}