package com.uni.spectro.ui.plot

import android.util.Log
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.bus.MessageType
import com.uni.spectro.domain.calculations.BatteryLevel

class CalibrationPresenter(private val view: CalibrationView) {

    fun handleMessageEvent(event: MessageEvent) {
        if (event.messageType == MessageType.BATTERY_LEVEL_CHANGED) {
            Log.i(TAG, "new battery level: " + event.content)
            setBatteryLevel(event.content)
        }
    }

    fun formatConcentration(concentration: Double): String {
        return "%.1f".format(concentration)
    }

    private fun setBatteryLevel(level: Int) {
        val scaled = BatteryLevel(level).level()
        view.updateBatteryLevel(scaled)
    }

    companion object {
        private val TAG = CalibrationPresenter::class.java.name
    }

}