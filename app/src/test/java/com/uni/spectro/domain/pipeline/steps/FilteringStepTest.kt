package com.uni.spectro.domain.pipeline.steps

import com.uni.spectro.domain.pipeline.Pipeline
import com.uni.spectro.domain.pipeline.model.PixelData
import org.assertj.core.api.Assertions
import org.junit.Test

class FilteringStepTest : PipelineStepTestBase() {

    @Test
    fun whenInvokeFilteringStep_thenApplyBoxFilterOnInputData() {
        // given
        val pipeline = Pipeline(FilteringStep(true))

        // when
        val output = pipeline.execute(PixelData(doubleArrayOf(40.0, 10.0, 10.0, 10.0, 10.0, 10.0, 40.0)))

        // then
        Assertions.assertThat(output.pixelData()).containsExactly(40.0, 10.0, 16.0, 10.0, 16.0, 10.0, 40.0)
    }

    @Test
    fun whenInvokeFilteringStep_thenLeaveLeadingAndTrailingPointsIntact() {
        // given
        val pipeline = Pipeline(FilteringStep(true))

        // when
        val output = pipeline.execute(PixelData(doubleArrayOf(40.0, 10.0, 10.0, 10.0, 10.0, 10.0, 40.0)))

        // then
        Assertions.assertThat(output.pixelData()).startsWith(40.0).endsWith(40.0)
    }

    @Test
    fun givenFilterTurnedOff_whenFilter_thenReturnOriginalDataPoints() {
        // given
        val pipeline = Pipeline(FilteringStep(false))

        // when
        val output = pipeline.execute(PixelData(doubleArrayOf(40.0, 10.0, 10.0, 10.0, 10.0, 10.0, 40.0)))

        // then
        Assertions.assertThat(output.pixelData()).containsExactly(40.0, 10.0, 10.0, 10.0, 10.0, 10.0, 40.0)
    }
}