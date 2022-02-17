package com.uni.spectro.bluetooth

import android.util.Log
import com.uni.spectro.constants.SpectrumConstants.PIXEL_COUNT
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.root.SpectroApplication
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

/**
 * This class serves as a container for the light intensity values read from the 288 pixels.
 * Once the container is full, the data is sent to the spectrum queue for later processing.
 */
class BleDataContainer(private val latch: CountDownLatch) {

    private var pixelCounter: AtomicInteger
    private var data: DoubleArray

    init {
        data = DoubleArray(PIXEL_COUNT)
        pixelCounter = AtomicInteger()
    }

    fun add(textDataPoint: String) {
        val value = textDataPoint.toInt()
        incrementAndAdd(value)
    }

    private fun incrementAndAdd(dataPoint: Int) {
        data[pixelCounter.getAndIncrement()] = dataPoint.toDouble()
        if (pixelCounter.get() == PIXEL_COUNT) {
            logData()
            SpectroApplication.spectrumStack().add(PixelData(data))
            Log.e(TAG, "message forwarded to stack")
            reset()
            sendSignal()
        }
    }

    private fun reset() {
        data = DoubleArray(PIXEL_COUNT)
        pixelCounter.set(0)
    }

    // send signal to the original data acquisition thread, that the Stack was populated with the result
    private fun sendSignal() {
        latch.countDown()
    }

    private fun logData() {
        val builder: StringBuilder = StringBuilder()
        data.map { point -> builder.append(point).append(" ") }
        Log.i(TAG, builder.toString())
    }

    private companion object {
        private val TAG = BleDataContainer::class.java.name
    }

}