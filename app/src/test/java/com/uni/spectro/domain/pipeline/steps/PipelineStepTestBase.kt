package com.uni.spectro.domain.pipeline.steps

import com.uni.spectro.domain.pipeline.model.PixelData

open class PipelineStepTestBase {
    companion object {
        @JvmStatic
        protected val INPUT_WITH_FOUR_DATA_POINTS = PixelData(doubleArrayOf(10.0, 20.0, 40.0, 50.0))
    }
}