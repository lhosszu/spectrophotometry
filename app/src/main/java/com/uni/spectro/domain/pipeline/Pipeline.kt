package com.uni.spectro.domain.pipeline

/**
 * Pipeline design patter for processing sensor data.
 */
class Pipeline<IN, OUT>(private val step: Step<IN, OUT>) {

    fun <NEXT> pipe(next: Step<OUT, NEXT>): Pipeline<IN, NEXT> {
        return Pipeline(object : Step<IN, NEXT> {
            override fun invoke(input: IN): NEXT {
                return next.invoke(step.invoke(input))
            }
        })
    }

    fun execute(input: IN): OUT {
        return step.invoke(input)
    }
}