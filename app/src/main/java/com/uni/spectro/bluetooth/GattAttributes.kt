package com.uni.spectro.bluetooth

import java.util.*

/**
 * Bluetooth Low Energy specific constants
 * (unique identifiers for services / characteristics)
 */
object GattAttributes {
    @JvmField
    val SPECTRUM_SERVICE_UUID: UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b")

    @JvmField
    val SPECTRUM_CHARACTERISTIC_UUID: UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")

    @JvmField
    val TRIGGER_SERVICE_UUID: UUID = UUID.fromString("62fb0057-e0ed-4a22-b906-7b2e848cdc0b")

    @JvmField
    val TRIGGER_CHARACTERISTIC_UUID: UUID = UUID.fromString("1556197c-4e2c-4840-9736-6c452a0e041e")

    @JvmField
    val BATTERY_SERVICE_UUID: UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")

    @JvmField
    val BATTERY_LEVEL_CHARACTERISTIC_UUID: UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")
}