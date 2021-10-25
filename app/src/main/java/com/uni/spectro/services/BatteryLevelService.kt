package com.uni.spectro.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BatteryLevelService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {}

    private fun broadcastBatteryLevel() {
        val intent = Intent()
        intent.action = "BATTERY_LEVEL_ACTION"
        intent.putExtra("BATTERY_LEVEL", 5)
        sendBroadcast(intent)
    }
}