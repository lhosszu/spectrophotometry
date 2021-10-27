package com.uni.spectro.ui.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.uni.spectro.bluetooth.BLEService
import com.uni.spectro.bluetooth.Connect
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.bus.MessageType
import com.uni.spectro.domain.calculations.BatteryLevel
import com.uni.spectro.domain.pipeline.SignalProcessingPipeline
import com.uni.spectro.domain.pipeline.model.Void
import com.uni.spectro.preferences.GlobalSettings
import com.uni.spectro.preferences.PreferenceManager
import com.uni.spectro.root.SpectroApplication
import java.lang.ref.WeakReference

class BluetoothPresenter(private val view: BluetoothView) {

    fun handleMessageEvent(event: MessageEvent, context: WeakReference<Context>) {
        when (event.messageType) {
            MessageType.BATTERY_LEVEL_CHANGED -> {
                Log.i(TAG, "new battery level: " + event.content)
                setBatteryLevel(event.content)
            }

            MessageType.DEVICE_CONNECTED -> {
                view.successfulDeviceConnection()
                SpectroApplication.executor().execute { SignalProcessingPipeline().background(context).execute(Void()) }
            }

            MessageType.DEVICE_DISCONNECTED -> {
                view.deviceDisconnected()
            }
        }
    }

    fun autoConnect(context: WeakReference<Context>) {
        if (!PreferenceManager.instance.getPreference(GlobalSettings.AUTO_CONNECT)) {
            Connect().connect(context)
        }
    }

    fun connect(device: BluetoothDevice, context: WeakReference<Context>) {
        Connect().connect(device, context)
    }

    fun btEnabled(): Boolean {
        return BLEService.instance.bluetoothEnabled()
    }

    private fun setBatteryLevel(level: Int) {
        val scaled = BatteryLevel(level).level()
        view.updateBatteryLevel(scaled)
    }

    private companion object {
        private val TAG = BluetoothPresenter::class.java.name
    }

}