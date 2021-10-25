package com.uni.spectro.domain.pipeline.steps

import com.uni.spectro.constants.SpectrumConstants
import com.uni.spectro.domain.pipeline.exception.PipelineInvocationException
import com.uni.spectro.domain.pipeline.model.PixelData
import org.assertj.core.api.Assertions
import org.junit.Test

class AnalyzeStepTest : PipelineStepTestBase() {

    @Test
    fun whenSelectExistingWavelength_thenReturnCorrespondingValue() {
        // given
        val selected = 676

        // when
        val result = AnalyzeStep(selected)
                .invoke(PixelData(DoubleArray(SpectrumConstants.PIXEL_COUNT)))
                .pixelData()[0]

        // then
        Assertions.assertThat(result).isEqualTo(0.0)
    }

    @Test(expected = PipelineInvocationException::class)
    fun whenSelectNonExistingWavelength_thenThrowException() {
        AnalyzeStep(10000).invoke(PixelData(DoubleArray(SpectrumConstants.PIXEL_COUNT)))
    }
}