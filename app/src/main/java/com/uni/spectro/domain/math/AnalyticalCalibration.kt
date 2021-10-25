package com.uni.spectro.domain.math

import com.uni.spectro.wrapper.ReportWrapper
import com.uni.spectro.persistence.model.RealmExperiment

/**
 * This class is responsible for using a simple linear interpolation algorithm to fit a linear curve
 * on selected CALIBRATION experiment points.
 * The estimated concentration is calculated for samples (experiments) with 0.0 concentration values.
 * A text report is generated from the results, that is shown on the "Report" page
 */
class AnalyticalCalibration(allExperiments: List<RealmExperiment>) {

    private val knownExperiments: List<RealmExperiment>
    private val unknownExperiments: List<RealmExperiment>

    init {
        this.knownExperiments = knownExperimentsSortedByConcentration(allExperiments)
        this.unknownExperiments = unknown(allExperiments)
    }

    fun report(): ReportWrapper {
        val interpolation = interpolate()
        val reportBuilder: StringBuilder = buildTextReport(interpolation)
        return ReportWrapper(interpolation.rSquared(), reportBuilder.toString())
    }

    private fun buildTextReport(interpolation: LinearInterpolation): StringBuilder {
        val reportBuilder: StringBuilder = StringBuilder()

        for (i in unknownExperiments.indices) {
            val experiment = unknownExperiments[i]
            val concentration = interpolation.xValueFor(experiment.selectedIntensity)

            reportBuilder.append("Experiment name:")
            reportBuilder.append("\n\t\t")
            reportBuilder.append(experiment.name)
            reportBuilder.append("\n")
            reportBuilder.append("Concentration:")
            reportBuilder.append("\n\t\t")
            reportBuilder.append(concentration)
            reportBuilder.append(" mg/L")
            reportBuilder.append("\n")

            if (i < unknownExperiments.size - 1) {
                reportBuilder.append("\n")
            }
        }
        return reportBuilder
    }

    private fun interpolate(): LinearInterpolation {
        val x = knownExperiments.map { it.concentration }.toDoubleArray()
        val y = knownExperiments.map { it.selectedIntensity }.toDoubleArray()
        return LinearInterpolation(x, y)
    }

    private fun knownExperimentsSortedByConcentration(allExperiments: List<RealmExperiment>): List<RealmExperiment> {
        return allExperiments.filter { it.concentration != 0.0 }.sortedWith(compareBy { it.concentration })
    }

    private fun unknown(allExperiments: List<RealmExperiment>): List<RealmExperiment> {
        return allExperiments.filter { it.concentration == 0.0 }
    }

}