package com.uni.spectro.domain.pipeline.steps.filter.impl

import com.uni.spectro.domain.pipeline.steps.filter.Filter
import java.math.BigDecimal
import java.math.RoundingMode

class GaussianBlur : Filter {

    private val coefficients = doubleArrayOf(
            0.0044,
            0.0540,
            0.2420,
            0.3991,
            0.2420,
            0.0540,
            0.0044
    )

    private val padding: Int = (coefficients.size - 1) / 2

    override fun filter(input: DoubleArray): DoubleArray {
        val filtered = DoubleArray(input.size)

        // keep leading and trailing points
        for (i in 0 until padding) {
            filtered[i] = input[i]
            filtered[input.size - 1 - i] = input[input.size - 1 - i]
        }

        runFilter(filtered, input)
        return filtered
    }

    private fun runFilter(filtered: DoubleArray, input: DoubleArray) {
        for (i in padding until filtered.size - padding) {
            var value = 0.0
            for (j in coefficients.indices) {
                value += input[i - padding + j] * coefficients[j]
            }
            filtered[i] = BigDecimal(value).setScale(4, RoundingMode.HALF_EVEN).toDouble()
        }
    }

}