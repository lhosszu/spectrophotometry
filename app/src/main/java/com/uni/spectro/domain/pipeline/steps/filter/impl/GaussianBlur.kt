package com.uni.spectro.domain.pipeline.steps.filter.impl

import com.uni.spectro.domain.pipeline.steps.filter.Filter

class GaussianBlur : Filter {

    private val coefficients = floatArrayOf(0.0001f, 0.0003f, 0.0007f, 0.0015f, 0.0028f, 0.0046f, 0.0067f, 0.0089f, 0.0105f,
            0.0111f, 0.0105f, 0.0089f, 0.0067f, 0.0046f, 0.0028f, 0.0015f, 0.0007f, 0.0003f, 0.0001f
    )

    private val padding: Int

    init {
        padding = (coefficients.size - 1) / 2
    }

    override fun filter(input: DoubleArray): DoubleArray {
        val filtered = DoubleArray(input.size)

        // keep leading and trailing points
        for (i in 0 until padding) {
            filtered[i] = input[i]
            filtered[input.size - 1 - i] = input[input.size - 1 - i]
        }
        for (i in padding until filtered.size - padding) {
            var value = 0f
            for (j in 0..6) {
                value += input[i - padding + j].toFloat() * coefficients[j]
            }
            filtered[i] = value.toDouble()
        }
        return filtered
    }
}