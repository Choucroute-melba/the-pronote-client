package moi.choucroutemelba.thepronoteclient.data.pronote.api

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class PronoteConverter: Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        return Converter<ResponseBody, Any> { value ->
            val adapter = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            adapter.convert(value)
        }
    }
}