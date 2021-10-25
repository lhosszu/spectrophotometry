package com.uni.spectro.domain.pipeline.steps

import com.uni.spectro.domain.calculations.SensorCalibration
import com.uni.spectro.domain.calculations.SpectrumMaxCalculator
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.exception.PipelineInvocationException
import com.uni.spectro.domain.pipeline.model.PixelData

/**
 * This step is responsible for finding the wavelength with maximum intensity.
 * This will be used for calibration, unless specified.
 */
class AnalyzeStep(private val selected: Int) : Step<PixelData, PixelData> {

    private val sensorCalibration: SensorCalibration = SensorCalibration()

    override operator fun invoke(input: PixelData): PixelData {
        // extracting maximum
        if (selected == 0) {
            val selected = SpectrumMaxCalculator(input).maxWavelengthAndIntensity()
            return PixelData(doubleArrayOf(selected.intensity()))
        }

        // extracting specified value
        val index = sensorCalibration.indexForWavelength(selected)
        if (index < 0 || index > 287) {
            throw PipelineInvocationException("Cannot select wavelength: $selected")
        }
        val value = input.pixelData()[index]
        return PixelData(doubleArrayOf(value))
    }

}