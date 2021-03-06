package com.uni.spectro.domain.experiments

import android.content.Context
import android.util.Pair
import android.widget.Toast
import com.uni.spectro.R
import com.uni.spectro.domain.pipeline.SignalProcessingPipeline
import com.uni.spectro.domain.pipeline.model.ExperimentDetails
import com.uni.spectro.domain.pipeline.model.Void
import java.lang.ref.WeakReference

/**
 * Absorbance experiment: calculate the absorbance spectrum from the reference spectrum and the
 * spectrum of the sample itself.
 */
class Absorbance(private val context: WeakReference<Context>, private val experimentName: String) {

    private val pipeline: SignalProcessingPipeline = SignalProcessingPipeline()
    private val experimentDetails: ExperimentDetails = ExperimentDetails()

    fun run() {
        val experiment = experimentDetails.absorbance(experimentName)
        val background = pipeline.queryPipeline("background")
        if (!background.isEmpty) {
            val sample = pipeline.collectPipeline().execute(Void())
            pipeline.absorbanceSpectrumPipeline(experiment, context).execute(Pair(background, sample))
        } else {
            Toast.makeText(context.get(), R.string.toast_missing_baseline, Toast.LENGTH_SHORT).show()
        }
    }
}