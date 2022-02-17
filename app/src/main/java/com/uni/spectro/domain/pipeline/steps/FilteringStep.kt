package com.uni.spectro.domain.pipeline.steps

import android.util.Log
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.domain.pipeline.steps.filter.Filter
import com.uni.spectro.domain.pipeline.steps.filter.impl.BoxFilter
import com.uni.spectro.preferences.GlobalSettings
import com.uni.spectro.preferences.PreferenceManager

/**
 * Runs box filter on input data points with a window size of (2 * padding + 1)
 * Leading and trailing points are left intact.
 */
class FilteringStep : Step<PixelData, PixelData> {

    private val blurring: Filter
    private val apply: Boolean

    constructor(blurring: Filter) {
        this.blurring = blurring
        this.apply = PreferenceManager.instance.getPreference(GlobalSettings.SMOOTH_SPECTRUM)
    }

    constructor(apply: Boolean) {
        this.blurring = BoxFilter()
        this.apply = apply
    }

    override operator fun invoke(input: PixelData): PixelData {
        Log.i(TAG, "Filtering spectrum")
        if (apply) {
            val output = blurring.filter(input.pixelData())
            Log.i(TAG, "Spectrum filtered")
            return PixelData(output)
        }
        Log.i(TAG, "Skip filtering")
        return input
    }

    private companion object {
        private val TAG = FilteringStep::class.java.name
    }
}