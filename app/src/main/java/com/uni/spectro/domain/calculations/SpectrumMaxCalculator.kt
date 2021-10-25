package com.uni.spectro.domain.calculations

import com.uni.spectro.domain.calculations.model.SelectedWavelength
import com.uni.spectro.domain.pipeline.model.PixelData

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