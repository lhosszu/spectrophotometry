package com.uni.spectro.domain.calculations

import kotlin.math.floor
import kotlin.math.log10

/**
 * This class is responsible for calculating the 'absorbance' from the:
 * - reference signal (light source emission + absorption of the sample holder without a sample)
 * - signal from the real sample (absorption of the analyzed compounds)
 */
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

    constructor(background: DoubleArray, signal: DoubleArray, limit: Int) {
        this.intensityBackground = background
        this.intensity = signal
        this.limit = limit - 1 //Kotlin's IntRange works with 'endInclusive' parameter
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
        return IntRange(0, limit).map { intensityBackground[it] / intensity[it] }.toDoubleArray()
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