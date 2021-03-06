package com.uni.spectro.domain.pipeline.steps

import android.content.Context
import android.content.Intent
import android.util.Log
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.persistence.model.ExperimentType
import com.uni.spectro.persistence.model.RealmExperiment
import com.uni.spectro.preferences.GlobalSettings
import com.uni.spectro.preferences.PreferenceManager
import com.uni.spectro.ui.plot.PlotActivity
import com.uni.spectro.wrapper.JsonConverter
import io.realm.RealmList
import java.lang.ref.WeakReference

/**
 * This step is responsible for visualizing the spectrum on the given context.
 */
class VisualizationStep(private val context: WeakReference<Context>) : Step<PixelData, PixelData> {

    private val preferenceManager: PreferenceManager = PreferenceManager.instance

    override operator fun invoke(input: PixelData): PixelData {
        Log.i(TAG, "Visualizing spectrum")
        visualize(input)
        return input
    }

    private fun visualize(pixelData: PixelData) {
        if (preferenceManager.getPreference(GlobalSettings.OPEN_AFTER_ACQUISITION)) {
            val realmList: RealmList<Double> = RealmList()
            pixelData.pixelData().forEach { realmList.add(it) }
            plotSpectrum(RealmExperiment("", realmList, ExperimentType.TRANSMITTANCE))
        }
    }

    private fun plotSpectrum(experiment: RealmExperiment) {
        val json = JsonConverter().toJson(experiment)
        val intent = Intent(context.get(), PlotActivity::class.java)
        intent.putExtra("VISUALIZE_EXPERIMENT_STRING", json)
        context.get()!!.startActivity(intent)
    }

    private companion object {
        private val TAG = VisualizationStep::class.java.name
    }
}