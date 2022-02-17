package com.uni.spectro.domain.pipeline.steps

import android.util.Log
import com.uni.spectro.constants.SpectrumConstants
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.exception.MalformedDataException
import com.uni.spectro.domain.pipeline.model.PixelData

/**
 * This step is responsible for checking if all data points have arrived from the sensor
 *
 * @see SpectrumConstants.PIXEL_COUNT
 */
class InputValidationStep : Step<PixelData, PixelData> {

    private val expectedSize: Int

    constructor(expectedSize: Int) {
        this.expectedSize = expectedSize
    }

    constructor() {
        expectedSize = SpectrumConstants.PIXEL_COUNT
    }

    override operator fun invoke(input: PixelData): PixelData {
        Log.i(TAG, "Validating data from sensor")
        checkLength(input.pixelData())
        return input
    }

    private fun checkLength(sensorData: DoubleArray) {
        if (sensorData.size != expectedSize) {
            throw MalformedDataException("Insufficient size of intensity points")
        }
        Log.i(TAG, "Number of data points is valid")
    }

    private companion object {
        private val TAG = InputValidationStep::class.java.name
    }
}