package com.uni.spectro.domain.experiments

import android.content.Context
import com.uni.spectro.domain.pipeline.SignalProcessingPipeline
import com.uni.spectro.domain.pipeline.model.ExperimentDetails
import com.uni.spectro.domain.pipeline.model.Void
import com.uni.spectro.root.SpectroApplication
import java.lang.ref.WeakReference
import java.util.concurrent.CompletableFuture

class Transmittance(private val context: WeakReference<Context>, private val experimentName: String) {

    private val pipeline: SignalProcessingPipeline = SignalProcessingPipeline()
    private val experimentDetails: ExperimentDetails = ExperimentDetails()

    fun run() {
        val experiment = experimentDetails.transmittance(experimentName)
        CompletableFuture.runAsync({
            pipeline.transmittanceSpectrumPipeline(experiment, context).execute(Void())
        }, SpectroApplication.executor())
    }
}