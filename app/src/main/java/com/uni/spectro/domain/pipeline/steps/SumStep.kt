package com.uni.spectro.domain.pipeline.steps

import android.content.Context
import android.widget.Toast
import com.uni.spectro.R
import com.uni.spectro.constants.SpectrumConstants.INTENSITY_SUM_LIMIT
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.model.PixelData
import java.lang.ref.WeakReference

/**
 * This pipeline handler is responsible for summing all intensity points.
 * The sum is used to validate if the emission of the lamp is sufficient.
 */
class SumStep(private var context: WeakReference<Context>?) : Step<PixelData, PixelData> {

    override fun invoke(input: PixelData): PixelData {
        val sum = input.pixelData().sum()
        if (sum < INTENSITY_SUM_LIMIT) {
            if (context != null) {
                Toast.makeText(context!!.get(), R.string.toast_low_lamp_intensity, Toast.LENGTH_SHORT).show()
            }
        }
        return input
    }
}