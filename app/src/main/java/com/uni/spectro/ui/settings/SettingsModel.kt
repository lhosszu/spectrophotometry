package com.uni.spectro.ui.settings

import android.util.Log
import android.widget.CompoundButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.uni.spectro.preferences.GlobalSettings
import com.uni.spectro.preferences.PreferenceManager

/**
 * This class is responsible for saving and loading preferences, and associating them
 * with the corresponding toggle/switch on the UI.
 */
class SettingsModel(private val switches: Map<GlobalSettings, SwitchMaterial>) {

    private val preferenceManager: PreferenceManager = PreferenceManager.instance

    fun loadSwitchStateFromPreferences() {
        for (setting in switches.keys) {
            val preference = preferenceManager.getPreference(setting)
            if (switches.containsKey(setting)) {
                val switcher = switches[setting]
                switcher!!.isChecked = preference
            }
        }
    }

    fun setSwitchListeners() {
        for (setting in switches.keys) {
            val switcher = switches[setting]
            switcher?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                preferenceManager.setPreference(setting, isChecked)
                logSwitchToggle(setting, isChecked)
            }
        }
    }

    fun saveSwitchStates() {
        for (setting in switches.keys) {
            val switcher = switches[setting]
            if (switcher != null) {
                val preference = switcher.isChecked
                preferenceManager.setPreference(setting, preference)
            }
        }
    }

    private fun logSwitchToggle(setting: GlobalSettings, isChecked: Boolean) {
        val state = if (isChecked) " ON" else " OFF"
        Log.i(TAG, setting.name + state)
    }

    companion object {
        private val TAG = SettingsModel::class.java.name
    }
}