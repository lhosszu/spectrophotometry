package com.uni.spectro.domain.pipeline.steps.filter

/**
 * The aim of the filter implementations is to blur the spectra before visualization.
 * Blurred spectra is not used for concentration calculations.
 */
interface Filter {
    fun filter(input: DoubleArray): DoubleArray
}