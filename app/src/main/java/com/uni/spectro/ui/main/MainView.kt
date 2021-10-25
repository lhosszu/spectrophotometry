package com.uni.spectro.ui.main

interface MainView {
    fun navigateToBluetoothPage()
    fun navigateToAcquisitionPage()
    fun navigateToExperimentsPage()
    fun navigateToSettingsPage()
    fun updateBatteryLevel(level: Int)
    fun successfulDeviceConnection()
    fun deviceDisconnected()
}