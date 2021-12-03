package com.uni.spectro.domain.pipeline

import android.content.Context
import android.util.Log
import android.util.Pair
import com.uni.spectro.domain.pipeline.exception.PipelineInvocationException
import com.uni.spectro.domain.pipeline.model.ExperimentDetails
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.domain.pipeline.model.Void
import com.uni.spectro.domain.pipeline.steps.*
import com.uni.spectro.domain.pipeline.steps.filter.impl.BoxFilter
import com.uni.spectro.domain.pipeline.steps.filter.impl.GaussianBlur
import com.uni.spectro.persistence.RealmPersistence
import java.lang.ref.WeakReference

/**
 * This class holds organized pipeline handlers for different purposes
 */
class SignalProcessingPipeline {

    private val acquisitionStep: AcquisitionStep

    constructor(acquisitionStep: AcquisitionStep) {
        this.acquisitionStep = acquisitionStep
    }

    constructor() {
        this.acquisitionStep = AcquisitionStep()
    }

    /**
     * Pipeline handlers for 'transmittance' experiments
     * Collect, process and persist spectrum of transmitted light.
     */
    fun transmittanceSpectrumPipeline(details: ExperimentDetails, visualizationContext: WeakReference<Context>): Pipeline<Void, PixelData> {
        return Pipeline(acquisitionStep)
                .pipe(InputValidationStep())
                .pipe(FilteringStep(GaussianBlur()))
                .pipe(RangeExtractionStep())
                .pipe(PersistenceStep(details))
                .pipe(VisualizationStep(visualizationContext))
    }

    /**
     * Pipeline handlers for data collection and size validation
     *  Input: -
     *  Output: PixelData object
     */
    fun collectPipeline(): Pipeline<Void, PixelData> {
        return Pipeline(acquisitionStep).pipe(InputValidationStep())
    }

    /**
     * Pipeline handlers for 'absorbance' experiments
     */
    fun absorbanceSpectrumPipeline(details: ExperimentDetails, visualizationContext: WeakReference<Context>): Pipeline<Pair<PixelData, PixelData>, PixelData> {
        return Pipeline(AbsorbanceCalculationStep())
                .pipe(InputValidationStep())
                .pipe(RangeExtractionStep())
                .pipe(PersistenceStep(details))
                .pipe(VisualizationStep(visualizationContext))
    }

    /**
     * Pipeline handlers for 'calibration' experiments
     */
    fun calibrationPipeline(details: ExperimentDetails): Pipeline<Pair<PixelData, PixelData>, PixelData> {
        return Pipeline(AbsorbanceCalculationStep())
                .pipe(AnalyzeStep(details.selectedWavelength()))
                .pipe(PersistenceStep(details))
    }

    /**
     * Pipeline handlers for background (light bulb emission) collection
     */
    fun background(context: WeakReference<Context>): Pipeline<Void, PixelData> {
        val experiment = ExperimentDetails().transmittance("background")
        return Pipeline(acquisitionStep)
                .pipe(InputValidationStep())
                .pipe(PersistenceStep(experiment))
                .pipe(LampTestingStep(context))
    }

    /**
     * Pipeline handler for querying a spectrum in the NoSQL database
     */
    fun queryPipeline(query: String): PixelData {
        return try {
            Pipeline(QueryStep(query)).execute(Void())
        } catch (e: PipelineInvocationException) {
            Log.e(TAG, "Cannot query DB for $query", e)
            PixelData(doubleArrayOf())
        }
    }

    fun queryPipeline(query: String, persistence: RealmPersistence): PixelData {
        return try {
            Pipeline(QueryStep(persistence, query)).execute(Void())
        } catch (e: PipelineInvocationException) {
            Log.e(TAG, "Cannot query DB for $query", e)
            PixelData(doubleArrayOf())
        }
    }

    private companion object {
        private val TAG = SignalProcessingPipeline::class.java.name
    }
}