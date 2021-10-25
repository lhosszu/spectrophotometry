package com.uni.spectro.domain.pipeline.steps

import com.uni.spectro.domain.pipeline.Pipeline
import com.uni.spectro.domain.pipeline.exception.MalformedDataException
import org.assertj.core.api.Assertions
import org.assertj.core.api.ThrowableAssert
import org.junit.Test

class InputValidationStepTest : PipelineStepTestBase() {

    @Test(expected = MalformedDataException::class)
    fun givenMissingDataPoints_whenInvokeInputValidationStep_thenThrowCustomException() {
        // when
        val pipeline = Pipeline(InputValidationStep(5))

        // then
        pipeline.execute(INPUT_WITH_FOUR_DATA_POINTS)
    }

    @Test
    fun givenNoMissingDataPoints_whenInvokeInputValidationStep_thenDoNotThrowCustomException() {
        // given
        val pipeline = Pipeline(InputValidationStep(4))

        // when
        val thrown = ThrowableAssert.catchThrowable { pipeline.execute(INPUT_WITH_FOUR_DATA_POINTS) }

        // then
        Assertions.assertThat(thrown).isNull()
    }
}