package com.uni.spectro.wrapper

import com.uni.spectro.persistence.model.RealmExperiment
import com.uni.spectro.persistence.util.RealmDataConverter.Companion.toArrayList
import java.time.Instant
import java.util.*

class ExperimentWrapper {

    var name: String = ""
    var date: Date = Date.from(Instant.now())
    var selectedWavelength: Int = 0
    var intensity: Double = 0.0
    var concentration: Double = 0.0
    var points: List<Double> = emptyList()
    var experimentType: String = ""

    constructor(experiment: RealmExperiment) {
        name = experiment.name
        date = experiment.date
        selectedWavelength = experiment.selectedWavelength
        intensity = experiment.selectedIntensity
        concentration = experiment.concentration
        points = toArrayList(experiment.dataPoints)
        experimentType = experiment.experimentType
    }

    constructor() {}
}