package com.uni.spectro.wrapper

import com.uni.spectro.persistence.model.RealmExperiment
import com.uni.spectro.persistence.util.RealmDataConverter.Companion.toArrayList
import java.util.*

class ExperimentWrapper {

    lateinit var name: String
    lateinit var date: Date
    var selectedWavelength: Int = 0
    var intensity: Double = 0.0
    var concentration: Double = 0.0
    lateinit var points: List<Double>
    lateinit var experimentType: String

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