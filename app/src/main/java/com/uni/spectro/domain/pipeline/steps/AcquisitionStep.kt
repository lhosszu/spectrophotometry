package com.uni.spectro.domain.pipeline.steps

import android.util.Log
import com.uni.spectro.bluetooth.BLEService
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.exception.PipelineInvocationException
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.domain.pipeline.model.Void
import com.uni.spectro.root.SpectroApplication
import java.util.concurrent.CountDownLatch

/**
 * This step is responsible for triggering Bluetooth data collection, and waiting for the result
 */
open class AcquisitionStep : Step<Void, PixelData> {

    private val latch: CountDownLatch = CountDownLatch(1)

    override operator fun invoke(input: Void): PixelData {
        sleep()
        trigger()
        awaitDataCollection()
        return pixelData
    }

    private val pixelData: PixelData
        get() {
            val message: PixelData = SpectroApplication.spectrumStack().pop()
            return PixelData(message.pixelData())
        }

    private fun sleep() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            Log.e(TAG, "${e.message}")
        }
    }

    private fun trigger() {
        Log.i(TAG, "Acquiring spectrum")
        BLEService.instance.triggerDataCollection(latch)
    }

    private fun awaitDataCollection() {
        try {
            latch.await()
        } catch (e: InterruptedException) {
            throw PipelineInvocationException("Problem with thread signaling during data acquisition: ", e)
        }
    }

    private companion object {
        private val TAG = AcquisitionStep::class.java.name
    }

}