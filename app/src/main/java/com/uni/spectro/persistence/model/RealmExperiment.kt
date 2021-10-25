package com.uni.spectro.persistence.model

import com.uni.spectro.domain.calculations.model.SelectedWavelength
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.time.Instant
import java.util.*

open class RealmExperiment : RealmObject {

    @PrimaryKey
    lateinit var name: String

    @Required
    lateinit var dataPoints: RealmList<Double>

    @Required
    var experimentType: String = ExperimentType.TRANSMITTANCE.name

    @Required
    var date: Date = Date.from(Instant.now())

    var selectedWavelength: Int = 0

    var selectedIntensity: Double = 0.0

    var concentration: Double = 0.0

    constructor()

    constructor(name: String, dataPoints: RealmList<Double>) {
        this.name = name
        this.dataPoints = dataPoints
    }

    constructor(name: String, dataPoints: RealmList<Double>, experimentType: ExperimentType) : super() {
        this.name = name
        this.dataPoints = dataPoints
        this.experimentType = experimentType.name
    }

    constructor(name: String, dataPoints: RealmList<Double>, experimentType: ExperimentType, concentration: Double) : super() {
        this.name = name
        this.dataPoints = dataPoints
        this.experimentType = experimentType.name
        this.concentration = concentration
    }

    constructor(name: String, dataPoints: RealmList<Double>, selectedWavelength: Int, maxIntensity: Double) : super() {
        this.name = name
        this.dataPoints = dataPoints
        this.experimentType = ExperimentType.CALIBRATION.name
        this.selectedWavelength = selectedWavelength
        this.selectedIntensity = maxIntensity
    }

    constructor(name: String, dataPoints: RealmList<Double>, experimentType: ExperimentType, selected: SelectedWavelength) : super() {
        this.name = name
        this.dataPoints = dataPoints
        this.experimentType = experimentType.name
        this.selectedWavelength = selected.wavelength()
        this.selectedIntensity = selected.intensity()
    }

    constructor(name: String, dataPoints: RealmList<Double>, experimentType: ExperimentType, selected: SelectedWavelength, concentration: Double) : super() {
        this.name = name
        this.dataPoints = dataPoints
        this.experimentType = experimentType.name
        this.selectedWavelength = selected.wavelength()
        this.selectedIntensity = selected.intensity()
        this.concentration = concentration
    }

    override fun toString(): String {
        return "RealmExperiment{" +
                "name='" + name + '\'' +
                ", dataPoints=" + dataPoints +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as RealmExperiment
        return this.name == other.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }
}