package com.uni.spectro.domain.pipeline.model

import com.uni.spectro.persistence.RealmPersistence
import com.uni.spectro.persistence.model.ExperimentType

/**
 * This class is responsible for holding data associated with a single experiment.
 * The light intensity data (pixel data) is kept separately.
 */
class ExperimentDetails {

    private lateinit var experimentName: String
    private lateinit var type: ExperimentType
    private lateinit var persistence: RealmPersistence
    private var selectedWavelength = 0

    constructor(experimentName: String, type: ExperimentType, selectedWavelength: Int) {
        this.experimentName = experimentName
        this.persistence = RealmPersistence()
        this.type = type
        this.selectedWavelength = selectedWavelength
    }

    constructor(experimentName: String, persistence: RealmPersistence) {
        this.experimentName = experimentName
        this.persistence = persistence
        this.type = ExperimentType.TRANSMITTANCE
        this.selectedWavelength = 0
    }

    constructor() {}

    fun transmittance(experimentName: String): ExperimentDetails {
        return ExperimentDetails(experimentName, ExperimentType.TRANSMITTANCE, 0)
    }

    fun absorbance(experimentName: String): ExperimentDetails {
        return ExperimentDetails(experimentName, ExperimentType.ABSORBANCE, 0)
    }

    fun calibration(experimentName: String, wavelength: Int): ExperimentDetails {
        return ExperimentDetails(experimentName, ExperimentType.CALIBRATION, wavelength)
    }

    fun experimentName(): String {
        return experimentName
    }

    fun persistence(): RealmPersistence {
        return persistence
    }

    fun type(): ExperimentType {
        return type
    }

    fun selectedWavelength(): Int {
        return selectedWavelength
    }
}