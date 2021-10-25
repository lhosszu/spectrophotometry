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

class PersistenceStep(private val details: ExperimentDetails) : Step<PixelData, PixelData> {

    override operator fun invoke(input: PixelData): PixelData {
        Log.i(TAG, "Persisting spectrum")

        val realmExperiment: RealmExperiment = if (analyze()) {
            val selected = SpectrumMaxCalculator(input).maxWavelengthAndIntensity()
            RealmExperiment(details.experimentName(), asRealmList(input), details.type(), selected, details.concentration())
        } else {
            if (details.type() == ExperimentType.CALIBRATION) {
                val selected = SelectedWavelength(details.selectedWavelength(), input.pixelData()[0])
                RealmExperiment(details.experimentName(), asRealmList(input), details.type(), selected, details.concentration())
            } else {
                RealmExperiment(details.experimentName(), asRealmList(input), details.type(), details.concentration())
            }
        }

        details.persistence().insert(realmExperiment) { Log.i(TAG, "Spectrum persisted") }
        return input
    }

    private fun asRealmList(input: PixelData): RealmList<Double> {
        val realmList: RealmList<Double> = RealmList()
        input.pixelData().forEach { realmList.add(it) }
        return realmList
    }

    private fun analyze(): Boolean {
        return PreferenceManager.instance.getPreference(GlobalSettings.ANALYZE_PEAKS)
    }

    companion object {
        private val TAG = PersistenceStep::class.java.name
    }
}