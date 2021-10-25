package com.uni.spectro.domain.calculations

import com.uni.spectro.domain.pipeline.steps.PipelineStepTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SpectrumMaxCalculatorTest : PipelineStepTestBase() {

    @Test
    fun canCalculateMaxWavelength() {
        // when
        val selected = SpectrumMaxCalculator(INPUT_WITH_FOUR_DATA_POINTS).maxWavelengthAndIntensity()

        // then
        assertThat(selected.wavelength()).isEqualTo(SensorCalibration().fullRange()[3])
    }

    @Test
    fun canCalculateMaxIntensity() {
        // when
        val selected = SpectrumMaxCalculator(INPUT_WITH_FOUR_DATA_POINTS).maxWavelengthAndIntensity()

        // then
        assertThat(selected.intensity()).isEqualTo(50.0)
        assertThat(selected.wavelength()).isEqualTo(324)
    }
}