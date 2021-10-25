package com.uni.spectro.ui.experiments

import com.uni.spectro.persistence.model.RealmExperiment

interface ExperimentsView {
    fun updateBatteryLevel(level: Int)
    fun plotSpectrum(experiment: RealmExperiment)
    fun plotCalibrationDetails(experiment: RealmExperiment)
    fun showReport(report: String)
}