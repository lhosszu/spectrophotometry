package com.uni.spectro.domain.pipeline.steps

import android.util.Log
import com.uni.spectro.domain.calculations.SpectrumMaxCalculator
import com.uni.spectro.domain.calculations.model.SelectedWavelength
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.model.ExperimentDetails
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.persistence.model.ExperimentType
import com.uni.spectro.persistence.model.RealmExperiment
import com.uni.spectro.preferences.GlobalSettings
import com.uni.spectro.preferences.PreferenceManager
import io.realm.RealmList

/**
 * This step is responsible for persisting the processed pixel data
 * Usually takes place before visualization
 */
class PersistenceStep(private val details: ExperimentDetails) : Step<PixelData, PixelData> {

    override operator fun invoke(input: PixelData): PixelData {
        Log.i(TAG, "Persisting spectrum")
        val experiment: RealmExperiment = createPersistableExperiment(input)
        persist(experiment)
        return input
    }

    private fun createPersistableExperiment(input: PixelData): RealmExperiment {
        val realmExperiment: RealmExperiment = if (analyze()) {
            analyzeSpectrum(input)
        } else {
            if (details.type() == ExperimentType.CALIBRATION) {
                calibrationSpectrum(input)
            } else {
                RealmExperiment(details.experimentName(), asRealmList(input), details.type(), details.concentration())
            }
        }
        return realmExperiment
    }

    private fun persist(realmExperiment: RealmExperiment) {
        details.persistence().insert(realmExperiment) { Log.i(TAG, "Spectrum persisted") }
    }

    private fun calibrationSpectrum(input: PixelData): RealmExperiment {
        val selected = SelectedWavelength(details.selectedWavelength(), input.pixelData()[0])
        return RealmExperiment(details.experimentName(), asRealmList(input), details.type(), selected, details.concentration())
    }

    private fun analyzeSpectrum(input: PixelData): RealmExperiment {
        val selected = SpectrumMaxCalculator(input).maxWavelengthAndIntensity()
        return RealmExperiment(details.experimentName(), asRealmList(input), details.type(), selected, details.concentration())
    }

    private fun asRealmList(input: PixelData): RealmList<Double> {
        val realmList: RealmList<Double> = RealmList()
        input.pixelData().forEach { realmList.add(it) }
        return realmList
    }

    private fun analyze(): Boolean {
        return PreferenceManager.instance.getPreference(GlobalSettings.ANALYZE_PEAKS)
    }

    private companion object {
        private val TAG = PersistenceStep::class.java.name
    }
}