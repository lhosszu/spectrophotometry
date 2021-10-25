package com.uni.spectro.preferences

/**
 * Settings as seen on the settings page. Values stored in SharedPreferences.
 */
enum class GlobalSettings(private val value: Boolean, private val identifier: String) {

    AUTO_CONNECT(false, "switch_auto_connect"),
    SMOOTH_SPECTRUM(false, "switch_smooth"),
    ANALYZE_PEAKS(false, "switch_analyze"),
    OPEN_AFTER_ACQUISITION(false, "switch_open_after"),
    SELECTED_WAVELENGTH(false, "switch_selected_wavelength");

    fun value(): Boolean {
        return value
    }

    fun identifier(): String {
        return identifier
    }
}