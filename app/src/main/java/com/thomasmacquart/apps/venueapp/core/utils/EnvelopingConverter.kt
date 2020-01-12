package com.thomasmacquart.apps.venueapp.core.utils

import com.squareup.moshi.Types
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Hides envelop implementation details into retrofit instead of leaking implementation details in the app
 */
class EnvelopingConverter : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val envelopedType : Type = Types.newParameterizedType(Envelope::class.java, type)
        val delegate : Converter<ResponseBody, Envelope<*>> = retrofit.nextResponseBodyConverter(this, envelopedType, annotations)
        return Converter<ResponseBody, Any> { body ->
            val envelope : Envelope<*>? = delegate.convert(body)
            envelope?.response
        }
    }
}