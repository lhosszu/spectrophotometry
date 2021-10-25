package com.uni.spectro.domain.pipeline.steps

import android.util.Log
import android.util.Pair
import com.uni.spectro.constants.SpectrumConstants
import com.uni.spectro.domain.calculations.AbsorbanceCalculator
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.model.PixelData

/**
 * This step is responsible for providing a toast notification about the result of the pipeline processing.
 * Input: a pair of spectra, the background and the transmittance of the sample itself.
 * Absorbance = log(I0/I)
 */
class AbsorbanceCalculationStep : Step<Pair<PixelData, PixelData>, PixelData> {

    override operator fun invoke(input: Pair<PixelData, PixelData>): PixelData {
        Log.i(TAG, "Calculating absorbance")
        val calculator = AbsorbanceCalculator(input.first.pixelData(), input.second.pixelData(), SpectrumConstants.PIXEL_COUNT)
        Log.i(TAG, "Absorbance calculated")
        return PixelData(calculator.absorbance())
    }

    private companion object {
        private val TAG = AbsorbanceCalculationStep::class.java.name
    }
}