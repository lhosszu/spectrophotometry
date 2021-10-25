package com.uni.spectro.ui.acquisition

interface AcquisitionView {
    fun transmittanceButtonClick()
    fun absorbanceButtonClick()
    fun calibrationButtonClick()
    fun updateBatteryLevel(level: Int)
}