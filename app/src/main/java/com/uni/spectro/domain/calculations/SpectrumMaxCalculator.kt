package com.uni.spectro.domain.calculations

import com.uni.spectro.domain.calculations.model.SelectedWavelength
import com.uni.spectro.domain.pipeline.model.PixelData

/**
 * This class uses the pixel data (aka. spectrum) to find the global intensity maximum
 * The maximum light intensity and corresponding wavelength are wrapped by a
 * SelectedWavelength object.
 * If maximum is not found, the selected wavelength is 0.
 */
class SpectrumMaxCalculator(private val data: PixelData) {

    private val sensorCalibration: SensorCalibration = SensorCalibration()

    fun maxWavelengthAndIntensity(): SelectedWavelength {
        val maxIntensity = data.pixelData().maxOrNull()

        for (i in data.pixelData().indices) {
            val point = data.pixelData()[i]
            if (point == maxIntensity) {
                val maxWavelength = sensorCalibration.fullRange()[i]
                return SelectedWavelength(maxWavelength, maxIntensity.toDouble())
            }
        }

        return SelectedWavelength(0, maxIntensity!!.toDouble())
    }
}