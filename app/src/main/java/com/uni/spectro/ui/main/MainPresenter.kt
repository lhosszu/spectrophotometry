package com.uni.spectro.ui.main

import android.content.Context
import android.util.Log
import com.uni.spectro.bluetooth.BLEService
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.bus.MessageType
import com.uni.spectro.domain.calculations.BatteryLevel
import com.uni.spectro.domain.pipeline.SignalProcessingPipeline
import com.uni.spectro.domain.pipeline.model.Void
import com.uni.spectro.root.SpectroApplication.Companion.executor
import java.lang.ref.WeakReference

class MainPresenter(private val mainView: MainView) {

    private val pipeline: SignalProcessingPipeline = SignalProcessingPipeline()

    fun handleMessageEvent(event: MessageEvent, context: WeakReference<Context>) {
        when (event.messageType) {
            MessageType.BATTERY_LEVEL_CHANGED -> {
                Log.i(TAG, "new battery level: " + event.content)
                setBatteryLevel(event.content)
            }

            MessageType.DEVICE_CONNECTED -> {
                mainView.successfulDeviceConnection()
                BLEService.instance.readBattery()
                executor().execute { pipeline.background(context).execute(Void()) }
            }

            MessageType.DEVICE_DISCONNECTED -> {
                mainView.deviceDisconnected()
            }
        }
    }

    private fun setBatteryLevel(level: Int) {
        val scaled = BatteryLevel(level).level()
        mainView.updateBatteryLevel(scaled)
    }

    private companion object {
        private val TAG = MainPresenter::class.java.name
    }

}