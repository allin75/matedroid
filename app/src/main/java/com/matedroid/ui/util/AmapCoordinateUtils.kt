package com.matedroid.ui.util

import com.amap.api.maps.model.LatLng
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val EARTH_SEMI_MAJOR_AXIS = 6378245.0
private const val EARTH_ECCENTRICITY = 0.00669342162296594323

/**
 * AMap renders overlays in GCJ-02, while TeslaMate coordinates are WGS84.
 * Convert before drawing to avoid visible route and marker offsets on the map.
 */
fun toAmapLatLng(latitude: Double, longitude: Double): LatLng {
    if (isOutsideChina(latitude, longitude)) {
        return LatLng(latitude, longitude)
    }

    val deltaLat = transformLatitude(longitude - 105.0, latitude - 35.0)
    val deltaLon = transformLongitude(longitude - 105.0, latitude - 35.0)
    val radLatitude = latitude / 180.0 * PI
    val magic = sin(radLatitude)
    val adjustedMagic = 1 - EARTH_ECCENTRICITY * magic * magic
    val sqrtMagic = sqrt(adjustedMagic)
    val latitudeOffset = (deltaLat * 180.0) /
        ((EARTH_SEMI_MAJOR_AXIS * (1 - EARTH_ECCENTRICITY)) / (adjustedMagic * sqrtMagic) * PI)
    val longitudeOffset = (deltaLon * 180.0) /
        (EARTH_SEMI_MAJOR_AXIS / sqrtMagic * cos(radLatitude) * PI)

    return LatLng(latitude + latitudeOffset, longitude + longitudeOffset)
}

private fun isOutsideChina(latitude: Double, longitude: Double): Boolean {
    return longitude !in 72.004..137.8347 || latitude !in 0.8293..55.8271
}

private fun transformLatitude(longitude: Double, latitude: Double): Double {
    var result = -100.0 + 2.0 * longitude + 3.0 * latitude + 0.2 * latitude * latitude
    result += 0.1 * longitude * latitude + 0.2 * sqrt(abs(longitude))
    result += (20.0 * sin(6.0 * longitude * PI) + 20.0 * sin(2.0 * longitude * PI)) * 2.0 / 3.0
    result += (20.0 * sin(latitude * PI) + 40.0 * sin(latitude / 3.0 * PI)) * 2.0 / 3.0
    result += (160.0 * sin(latitude / 12.0 * PI) + 320.0 * sin(latitude * PI / 30.0)) * 2.0 / 3.0
    return result
}

private fun transformLongitude(longitude: Double, latitude: Double): Double {
    var result = 300.0 + longitude + 2.0 * latitude + 0.1 * longitude * longitude
    result += 0.1 * longitude * latitude + 0.1 * sqrt(abs(longitude))
    result += (20.0 * sin(6.0 * longitude * PI) + 20.0 * sin(2.0 * longitude * PI)) * 2.0 / 3.0
    result += (20.0 * sin(longitude * PI) + 40.0 * sin(longitude / 3.0 * PI)) * 2.0 / 3.0
    result += (150.0 * sin(longitude / 12.0 * PI) + 300.0 * sin(longitude / 30.0 * PI)) * 2.0 / 3.0
    return result
}
