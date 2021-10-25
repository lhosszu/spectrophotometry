package com.uni.spectro.domain.pipeline

/**
 * A step (handler) in the signal processing pipeline.
 * Has generic input/output types.
 */
interface Step<IN, OUT> {
    operator fun invoke(input: IN): OUT
}