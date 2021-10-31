package com.uni.spectro.domain.pipeline.steps

import android.content.Context
import android.widget.Toast
import com.uni.spectro.R
import com.uni.spectro.constants.SpectrumConstants.INTENSITY_SUM_LIMIT
import com.uni.spectro.constants.SpectrumConstants.INTENSITY_TOLERANCE_PERCENT
import com.uni.spectro.constants.SpectrumConstants.THEORETICAL_INTENSITY_MAX
import com.uni.spectro.domain.calculations.SpectrumMaxCalculator
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.model.PixelData
import java.lang.ref.WeakReference

/**
 * This pipeline handler is responsible for validating if the emission of the lamp is sufficient.
 * Validation consists of two things:
 * - summing all intensity points, and comparing it with a predefined value (INTENSITY_SUM_LIMIT)
 * - comparing the maximum intensity of the spectrum to the theoretical limit
 * This is used for background collection, lamp emission validation
 */
class LampTestingStep(private var context: WeakReference<Context>?) : Step<PixelData, PixelData> {

    private val maxIntensityLimit = THEORETICAL_INTENSITY_MAX * INTENSITY_TOLERANCE_PERCENT

    override fun invoke(input: PixelData): PixelData {
        val max = SpectrumMaxCalculator(input).maxWavelengthAndIntensity()
        val sum = input.pixelData().sum()

        if (sum < INTENSITY_SUM_LIMIT || max.intensity() > maxIntensityLimit) {
            if (context != null) {
                Toast.makeText(context!!.get(), R.string.toast_lamp_emission_problem, Toast.LENGTH_SHORT).show()
            }
        }
        return input
    }
}