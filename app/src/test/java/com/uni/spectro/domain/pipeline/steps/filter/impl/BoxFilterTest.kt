package com.uni.spectro.domain.pipeline.steps.filter.impl

import com.uni.spectro.domain.pipeline.steps.PipelineStepTestBase
import org.assertj.core.api.Assertions
import org.junit.Test

class BoxFilterTest : PipelineStepTestBase() {
    @Test
    fun whenRunBoxFilter_thenKeepLeadingAndTrailingPoints() {
        // given
        val input = doubleArrayOf(1000.0, 2000.0, 3000.0, 2000.0, 1000.0, 3000.0, 2000.0, 5000.0, 6000.0)

        // when
        val result = BoxFilter(5).filter(input)

        // then
        Assertions.assertThat(result).endsWith(5000.0, 6000.0)
        Assertions.assertThat(result).startsWith(1000.0, 2000.0)
    }
}