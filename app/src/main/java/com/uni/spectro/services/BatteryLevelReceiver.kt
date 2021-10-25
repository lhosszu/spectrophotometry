package com.uni.spectro.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.function.Consumer

class BatteryLevelReceiver(private val onReceive: Consumer<Int>) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "BATTERY_LEVEL_ACTION") {
            val level = intent.getIntExtra("BATTERY_LEVEL", 0)
            Log.i(TAG, "new battery level: $level")
            onReceive.accept(level)
        }
    }

    companion object {
        private val TAG = BatteryLevelReceiver::class.java.name
    }
}