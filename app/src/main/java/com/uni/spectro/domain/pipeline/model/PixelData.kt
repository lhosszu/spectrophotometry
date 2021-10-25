package com.uni.spectro.domain.pipeline.model

/**
 * Wrapper object for holding an array of values associated with pixels of the sensor.
 */
class PixelData(private val pixelData: DoubleArray) {

    fun pixelData(): DoubleArray {
        return pixelData
    }

    fun length(): Int {
        return pixelData.size
    }

    override fun toString(): String {
        return pixelData.contentToString()
    }

    val isEmpty: Boolean get() = pixelData.isEmpty()
}