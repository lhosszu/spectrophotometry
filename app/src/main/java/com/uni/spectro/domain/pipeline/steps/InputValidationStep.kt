package com.uni.spectro.domain.pipeline.steps

import android.util.Log
import com.uni.spectro.constants.SpectrumConstants
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.exception.MalformedDataException
import com.uni.spectro.domain.pipeline.model.PixelData

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

    companion object {
        private val TAG = InputValidationStep::class.java.name
    }
}