package com.uni.spectro.domain.math

import com.uni.spectro.domain.calculations.model.SelectedWavelength
import com.uni.spectro.persistence.model.ExperimentType
import com.uni.spectro.persistence.model.RealmExperiment
import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AnalyticalCalibrationTest {

    private val allExperiments = listOf(
            RealmExperiment("B12 100 mg/L", RealmList<Double>(), ExperimentType.CALIBRATION, SelectedWavelength(555, 0.1229), 100.0),
            RealmExperiment("B12 50 mg/L", RealmList<Double>(), ExperimentType.CALIBRATION, SelectedWavelength(555, 0.0718), 50.0),
            RealmExperiment("B12 33.3 mg/L", RealmList<Double>(), ExperimentType.CALIBRATION, SelectedWavelength(555, 0.0548), 33.3),
            RealmExperiment("B12 25.0 mg/L", RealmList<Double>(), ExperimentType.CALIBRATION, SelectedWavelength(555, 0.0501), 25.0),
            RealmExperiment("B12 16.7 mg/L", RealmList<Double>(), ExperimentType.CALIBRATION, SelectedWavelength(555, 0.0405), 16.7),
            RealmExperiment("B12 12.5 mg/L", RealmList<Double>(), ExperimentType.CALIBRATION, SelectedWavelength(555, 0.0315), 12.5),
            RealmExperiment("B12 10.0 mg/L", RealmList<Double>(), ExperimentType.CALIBRATION, SelectedWavelength(555, 0.026), 10.0),
            RealmExperiment("B12 6.3 mg/L", RealmList<Double>(), ExperimentType.CALIBRATION, SelectedWavelength(555, 0.0174), 6.3),
            RealmExperiment("B12 ismeretlen 1", RealmList<Double>(), ExperimentType.CALIBRATION, SelectedWavelength(555, 0.1046), 0.0),
            RealmExperiment("B12 ismeretlen 2", RealmList<Double>(), ExperimentType.CALIBRATION, SelectedWavelength(555, 0.0236), 0.0)
    )

    @Test
    fun canGenerateCalibrationReport() {
        // given
        val calibration = AnalyticalCalibration(allExperiments)

        // when
        val report = calibration.report()

        // then
        assertThat(report.concentrations).contains("B12 ismeretlen 1").contains("B12 ismeretlen 2")
        assertThat(report.rSquared).isEqualTo("R-squared: 0.9851")
    }
}