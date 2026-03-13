package com.matedroid.data.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@JsonQualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FlexibleString

class FlexibleStringAdapter {
    @FromJson
    @FlexibleString
    fun fromJson(reader: JsonReader): String? {
        return when (reader.peek()) {
            JsonReader.Token.NULL -> {
                reader.nextNull<Unit>()
                null
            }

            JsonReader.Token.STRING -> reader.nextString()

            JsonReader.Token.BEGIN_ARRAY -> {
                reader.beginArray()
                var firstValue: String? = null
                while (reader.hasNext()) {
                    if (firstValue == null && reader.peek() == JsonReader.Token.STRING) {
                        firstValue = reader.nextString()
                    } else {
                        reader.skipValue()
                    }
                }
                reader.endArray()
                firstValue
            }

            JsonReader.Token.BEGIN_OBJECT -> {
                reader.skipValue()
                null
            }

            else -> {
                reader.skipValue()
                null
            }
        }
    }

    @ToJson
    fun toJson(@FlexibleString value: String?): String? = value
}

@JsonClass(generateAdapter = true)
data class AmapReverseGeocodeResponse(
    val status: String? = null,
    val info: String? = null,
    val regeocode: AmapRegeocodeResult? = null
)

@JsonClass(generateAdapter = true)
data class AmapRegeocodeResult(
    @Json(name = "formatted_address") val formattedAddress: String? = null,
    @Json(name = "addressComponent") val addressComponent: AmapAddressComponent? = null
)

@JsonClass(generateAdapter = true)
data class AmapAddressComponent(
    @FlexibleString val country: String? = null,
    @FlexibleString val province: String? = null,
    @FlexibleString val city: String? = null,
    @FlexibleString val district: String? = null,
    @FlexibleString val township: String? = null,
    @Json(name = "streetNumber") val streetNumber: AmapStreetNumber? = null
)

@JsonClass(generateAdapter = true)
data class AmapStreetNumber(
    @FlexibleString val street: String? = null,
    val number: String? = null
)

interface AmapWebServiceApi {
    @GET("v3/geocode/regeo")
    suspend fun reverseGeocode(
        @Query("location") location: String,
        @Query("key") key: String,
        @Query("extensions") extensions: String = "base",
        @Query("radius") radius: Int = 200,
        @Query("roadlevel") roadLevel: Int = 0
    ): Response<AmapReverseGeocodeResponse>
}
