package com.matedroid.ui.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AmapCoordinateUtilsTest {

    @Test
    fun toAmapLatLng_keepsCoordinatesOutsideChinaUnchanged() {
        val berlinLatitude = 52.520008
        val berlinLongitude = 13.404954

        val converted = toAmapLatLng(berlinLatitude, berlinLongitude)

        assertEquals(berlinLatitude, converted.latitude, 0.000001)
        assertEquals(berlinLongitude, converted.longitude, 0.000001)
    }

    @Test
    fun toAmapLatLng_convertsTypicalMainlandChinaCoordinates() {
        val converted = toAmapLatLng(
            latitude = 39.908823,
            longitude = 116.39747
        )

        assertTrue(converted.latitude in 39.9095..39.9110)
        assertTrue(converted.longitude in 116.402..116.405)
    }
}
