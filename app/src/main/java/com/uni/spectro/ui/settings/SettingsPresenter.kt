package com.uni.spectro.ui.settings

import android.util.Log
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.bus.MessageType
import com.uni.spectro.domain.calculations.BatteryLevel

class SettingsPresenter(private val view: SettingsView, model: SettingsModel) {

    private val settingsModel: SettingsModel = model

    fun handleMessageEvent(event: MessageEvent) {
        if (event.messageType == MessageType.BATTERY_LEVEL_CHANGED) {
            Log.i(TAG, "new battery level: " + event.content)
            setBatteryLevel(event.content)
        }
    }

    fun setSwitchListeners() {
        settingsModel.setSwitchListeners()
    }

    fun saveSwitchStates() {
        settingsModel.saveSwitchStates()
    }

    fun loadSwitchStateFromPreferences() {
        settingsModel.loadSwitchStateFromPreferences()
    }

    private fun setBatteryLevel(level: Int) {
        val scaled = BatteryLevel(level).level()
        view.updateBatteryLevel(scaled)
    }

    companion object {
        private val TAG = SettingsPresenter::class.java.name
    }
}