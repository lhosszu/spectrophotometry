package com.uni.spectro.domain.pipeline.steps

import android.util.Log
import com.uni.spectro.constants.SpectrumConstants
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.model.PixelData

/**
 * Removes leading and trailing data points to match the desired wavelength range.
 */
class RangeExtractionStep : Step<PixelData, PixelData> {

    private val offset: Int
    private val limit: Int

    constructor(offset: Int, limit: Int) {
        this.offset = offset
        this.limit = limit
    }

    constructor() {
        offset = SpectrumConstants.VISIBLE_RANGE_LOWER_LIMIT
        limit = SpectrumConstants.VISIBLE_RANGE_SIZE
    }

    override operator fun invoke(input: PixelData): PixelData {
        Log.i(TAG, "Extracting range")
        val range = selectRange(input.pixelData())
        Log.i(TAG, "Range extracted")
        return PixelData(range)
    }

    private fun selectRange(input: DoubleArray): DoubleArray {
        return input.drop(offset).take(limit).toDoubleArray()
    }

    companion object {
        private val TAG = RangeExtractionStep::class.java.name
    }
}