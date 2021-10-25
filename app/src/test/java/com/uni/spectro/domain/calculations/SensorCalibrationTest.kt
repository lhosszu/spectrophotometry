package com.uni.spectro.domain.calculations

import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class SensorCalibrationTest {

    private lateinit var sensorCalibration: SensorCalibration

    @Before
    fun setUp() {
        sensorCalibration = SensorCalibration()
    }

    @Test
    fun canConvertFirstPixelToNanometer() {
        // when
        val nanometer = sensorCalibration.nanometerFor(FIRST_PIXEL_INDEX)

        // then
        Assertions.assertThat(nanometer).isEqualTo(FIRST_PIXEL_NANOMETER)
    }

    @Test
    fun canConvertLastPixelToNanometer() {
        // when
        val nanometer = sensorCalibration.nanometerFor(LAST_PIXEL_INDEX)

        // then
        Assertions.assertThat(nanometer).isEqualTo(LAST_PIXEL_NANOMETER)
    }

    @Test
    fun canGetPixelIndexForWavelength() {
        // given
        val wavelengthInRange = 444

        // when
        val index = sensorCalibration.indexForWavelength(wavelengthInRange)

        // then
        Assertions.assertThat(index).isEqualTo(49)
    }

    @Test
    fun whenGetIndexForInvalidWavelength_thenReturnMinusValue() {
        // given
        val wavelengthInRange = 1444

        // when
        val index = sensorCalibration.indexForWavelength(wavelengthInRange)

        // then
        Assertions.assertThat(index).isNegative
    }

    companion object {
        private const val FIRST_PIXEL_INDEX = 0
        private const val LAST_PIXEL_INDEX = 287
        private const val FIRST_PIXEL_NANOMETER = 314
        private const val LAST_PIXEL_NANOMETER = 883
    }
}