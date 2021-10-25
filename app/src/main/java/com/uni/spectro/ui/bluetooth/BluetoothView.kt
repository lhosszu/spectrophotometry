package com.uni.spectro.ui.bluetooth

interface BluetoothView {
    fun updateBatteryLevel(level: Int)
    fun successfulDeviceConnection()
    fun deviceDisconnected()
    fun promptEnableBluetooth()
    fun promptEnableLocationAccess()
}