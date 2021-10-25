package com.uni.spectro.bluetooth

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
                Log.i("AutoConnect", "auto connecting to bluetooth device")
                ble.connect(context)
            }
        }
    }

    fun connect(context: WeakReference<Context>) {
        if (ble.bluetoothEnabled()) {
            SpectroApplication.executor().execute { ble.connect(context) }
        }
    }
}