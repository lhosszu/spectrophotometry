package com.uni.spectro.domain.pipeline.steps.filter.impl

import com.uni.spectro.constants.SpectrumConstants
import com.uni.spectro.domain.pipeline.steps.filter.Filter

class BoxFilter : Filter {

    private val windowSize: Int
    private val padding: Int

    constructor(windowSize: Int) {
        this.windowSize = windowSize
        padding = (windowSize - 1) / 2
    }

    constructor() {
        windowSize = SpectrumConstants.BOX_FILTER_WINDOW_SIZE
        padding = SpectrumConstants.BOX_FILTER_PADDING
    }

    override fun filter(input: DoubleArray): DoubleArray {
        val size = input.size
        val filtered = runFilter(input, size)

        // keep leading and trailing points
        for (i in 0 until padding) {
            filtered[i] = input[i]
            filtered[size - 1 - i] = input[size - 1 - i]
        }
        return filtered
    }

    private fun runFilter(input: DoubleArray, size: Int): DoubleArray {
        val filtered = DoubleArray(size)
        for (i in padding until size - padding) {
            var sum = 0.0
            for (j in 0 until windowSize) {
                sum += input[i - padding + j]
            }
            filtered[i] = sum / windowSize
        }
        return filtered
    }
}