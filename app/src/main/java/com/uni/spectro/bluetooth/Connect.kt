package com.uni.spectro.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.DEVICE_TYPE_LE
import android.content.Context
import android.util.Log
import com.uni.spectro.preferences.GlobalSettings
import com.uni.spectro.preferences.PreferenceManager
import com.uni.spectro.root.SpectroApplication
import java.lang.ref.WeakReference

class Connect {

    private val ble = BLEService.instance

    fun autoConnect(context: WeakReference<Context>) {
        if (PreferenceManager.instance.getPreference(GlobalSettings.AUTO_CONNECT) && ble.bluetoothEnabled()) {
            if (ble.bluetoothEnabled()) {
                Log.i(TAG, "Auto connecting to bluetooth device")
                ble.connect(context)
            }
        }
    }

    fun connect(context: WeakReference<Context>) {
        if (ble.bluetoothEnabled()) {
            SpectroApplication.executor().execute { ble.connect(context) }
        }
    }

    fun connect(device: BluetoothDevice, context: WeakReference<Context>) {
        if (ble.bluetoothEnabled()) {
            if (device.type == DEVICE_TYPE_LE) {
                SpectroApplication.executor().execute { ble.connect(device, context) }
            } else {
                Log.e(TAG, "Not a BLE device")
            }
        }
    }

    private companion object {
        private val TAG = Connect::class.java.name
    }
}