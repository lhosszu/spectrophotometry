package com.uni.spectro.domain.pipeline.steps.filter

interface Filter {
    fun filter(input: DoubleArray): DoubleArray
}