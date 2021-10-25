package com.uni.spectro.domain.calculations

import com.uni.spectro.constants.SpectrumConstants.VISIBLE_RANGE_SIZE
import kotlin.math.floor
import kotlin.math.log10

class AbsorbanceCalculator {

    /**
     * Intensities of incident light, I0 (without sample)
     */
    private var intensityBackground: DoubleArray

    /**
     * Intensities measured with sample (I)
     */
    private var intensity: DoubleArray

    /**
     * The number of processed pixels
     */
    private var limit: Int

    constructor(background: DoubleArray, signal: DoubleArray) {
        this.intensityBackground = background
        this.intensity = signal
        this.limit = VISIBLE_RANGE_SIZE - 1 //Kotlin's IntRange works with 'endInclusive' parameter
    }

    constructor(background: DoubleArray, signal: DoubleArray, limit: Int) {
        this.intensityBackground = background
        this.intensity = signal
        this.limit = limit - 1
    }

    /**
     * A = -log(T) = log(1/T)
     */
    fun absorbance(): DoubleArray {
        correctI()
        return log10(reciprocalTransmittance())
    }

    /**
     * T = I/I0 [%]
     * 1/T = I0/I [%]
     */
    private fun reciprocalTransmittance(): DoubleArray {
        return IntRange(0, limit).map { intensityBackground[it].toDouble() / intensity[it].toDouble() }.toDoubleArray()
    }

    /**
     * I cannot be larger than I0.
     * I cannot be 0.
     */
    private fun correctI() {
        IntRange(0, limit).forEach { i: Int ->
            if (intensity[i] > intensityBackground[i]) {
                intensity[i] = intensityBackground[i]
            } else if (intensity[i] == 0.0) {
                intensity[i] = 1.0
            }
        }
    }

    private fun log10(input: DoubleArray): DoubleArray {
        return input.map { log10(it) }.map { floor(it * 100.0) / 100.0 }.toDoubleArray()
    }
}