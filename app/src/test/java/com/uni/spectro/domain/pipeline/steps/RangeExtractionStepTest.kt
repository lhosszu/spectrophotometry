package com.uni.spectro.domain.pipeline.steps

import com.uni.spectro.domain.pipeline.Pipeline
import org.assertj.core.api.Assertions
import org.junit.Test

class RangeExtractionStepTest : PipelineStepTestBase() {

    @Test
    fun whenInvokeWavelengthRangeExtractionStep_thenCutDesiredRangeFromInput() {
        // given
        val pipeline = Pipeline(RangeExtractionStep(1, 2))

        // when
        val output = pipeline.execute(PipelineStepTestBase.Companion.INPUT_WITH_FOUR_DATA_POINTS)

        // then
        Assertions.assertThat(output.pixelData()).containsExactly(20.0, 40.0)
    }
}