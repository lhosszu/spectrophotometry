package com.uni.spectro.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager private constructor() {

    private lateinit var sharedPreferences: SharedPreferences

    private object HOLDER {
        val INSTANCE = PreferenceManager()
    }

    companion object {
        val instance: PreferenceManager by lazy { HOLDER.INSTANCE }
    }

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    fun initialize(preferences: SharedPreferences) {
        sharedPreferences = preferences
    }

    fun setPreference(setting: GlobalSettings, value: Boolean) {
        sharedPreferences.edit().putBoolean(setting.name, value).apply()
    }

    fun getPreference(setting: GlobalSettings): Boolean {
        return sharedPreferences.getBoolean(setting.name, setting.value())
    }
}