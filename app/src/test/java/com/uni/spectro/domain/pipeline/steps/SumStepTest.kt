package com.uni.spectro.domain.pipeline.steps

import com.uni.spectro.domain.pipeline.Pipeline
import org.assertj.core.api.Assertions
import org.junit.Test

class SumStepTest : PipelineStepTestBase() {

    @Test
    fun canInvokeSumStep() {
        // given
        val pipeline = Pipeline(SumStep(null))

        // when
        val output = pipeline.execute(INPUT_WITH_FOUR_DATA_POINTS)

        // then
        Assertions.assertThat(output.pixelData()).containsExactly(10.0, 20.0, 40.0, 50.0)
    }
}