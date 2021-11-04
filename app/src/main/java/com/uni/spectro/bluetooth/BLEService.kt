package com.uni.spectro.bluetooth

import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.uni.spectro.bluetooth.GattAttributes.BATTERY_LEVEL_CHARACTERISTIC_UUID
import com.uni.spectro.bluetooth.GattAttributes.BATTERY_SERVICE_UUID
import com.uni.spectro.bluetooth.GattAttributes.SPECTRUM_CHARACTERISTIC_UUID
import com.uni.spectro.bluetooth.GattAttributes.SPECTRUM_SERVICE_UUID
import com.uni.spectro.bluetooth.GattAttributes.TRIGGER_CHARACTERISTIC_UUID
import com.uni.spectro.bluetooth.GattAttributes.TRIGGER_SERVICE_UUID
import com.uni.spectro.bluetooth.exception.CharacteristicSubscribeException
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.bus.MessageType
import org.greenrobot.eventbus.EventBus
import java.lang.ref.WeakReference
import java.nio.charset.StandardCharsets
import java.util.concurrent.CountDownLatch

class BLEService private constructor() {

    private lateinit var gatt: BluetoothGatt
    private lateinit var trigger: BluetoothGattCharacteristic
    private lateinit var batteryLevel: BluetoothGattCharacteristic
    private lateinit var dataContainer: BleDataContainer

    private object HOLDER {
        val INSTANCE = BLEService()
    }

    companion object {
        private val TAG = BLEService::class.java.name
        val instance: BLEService by lazy { HOLDER.INSTANCE }
    }

    fun connect(context: WeakReference<Context>) {
        val device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice("24:62:AB:FD:30:86")
        if (device.type == BluetoothDevice.DEVICE_TYPE_UNKNOWN) {
            Log.i(TAG, "device not cached, scanning")
        } else {
            Log.i(TAG, "device cached")
            gatt = device.connectGatt(context.get(), true, GattCallback(), BluetoothDevice.TRANSPORT_LE)
        }
    }

    fun connect(device: BluetoothDevice, context: WeakReference<Context>) {
        gatt = device.connectGatt(context.get(), true, GattCallback(), BluetoothDevice.TRANSPORT_LE)
    }

    fun bluetoothEnabled(): Boolean {
        return BluetoothAdapter.getDefaultAdapter().isEnabled
    }

    private fun initSpectrumService(spectrumService: BluetoothGattService?) {
        Log.e(TAG, "initializing spectrum data characteristic")
        if (spectrumService != null) {
            val spectrumData = spectrumService.getCharacteristic(SPECTRUM_CHARACTERISTIC_UUID)
            val descriptor = spectrumData.descriptors[0]
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            spectrumData.addDescriptor(descriptor)
            gatt.writeDescriptor(descriptor)
            gatt.setCharacteristicNotification(spectrumData, true)
        } else {
            throw CharacteristicSubscribeException("Cannot subscribe to SPECTRUM characteristic, GATT service is null")
        }
    }

    private fun initTriggerService(triggerService: BluetoothGattService?) {
        Log.e(TAG, "initializing trigger characteristic")
        if (triggerService != null) {
            trigger = triggerService.getCharacteristic(TRIGGER_CHARACTERISTIC_UUID)
            trigger.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
        } else {
            throw CharacteristicSubscribeException("Cannot reach to TRIGGER characteristic, GATT service is null")
        }
    }

    private fun initBatteryService(batteryService: BluetoothGattService?) {
        Log.e(TAG, "initializing battery service")
        batteryLevel = if (batteryService != null) {
            batteryService.getCharacteristic(BATTERY_LEVEL_CHARACTERISTIC_UUID)
        } else {
            throw CharacteristicSubscribeException("Cannot init BATTERY characteristic, GATT service is null")
        }
    }

    fun triggerDataCollection(latch: CountDownLatch?) {
        dataContainer = BleDataContainer(latch!!)
        Log.e(TAG, "writing trigger value")
        trigger.value = "1".toByteArray()
        gatt.writeCharacteristic(trigger)
    }

    fun readBattery() {
        if (bluetoothEnabled()) {
            try {
                gatt.readCharacteristic(batteryLevel)
            } catch (e: Exception) {
                Log.e(TAG, "Cannot read battery level: ${e.message}")
            }
        }
        Log.i(TAG, "Cannot read battery level, BT disabled")
    }

    private inner class GattCallback : BluetoothGattCallback() {

        private val TAG = GattCallback::class.java.name

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Log.i(TAG, "connection state changed")
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED || newState == BluetoothProfile.STATE_DISCONNECTING) {
                EventBus.getDefault().post(MessageEvent(0, MessageType.DEVICE_DISCONNECTED))
                gatt.close()
            } else {
                gatt.close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            val spectrum = gatt.getService(SPECTRUM_SERVICE_UUID)
            val trigger = gatt.getService(TRIGGER_SERVICE_UUID)
            val battery = gatt.getService(BATTERY_SERVICE_UUID)
            Log.i(TAG, "services discovered")
            initSpectrumService(spectrum)
            initTriggerService(trigger)
            initBatteryService(battery)
            EventBus.getDefault().post(MessageEvent(0, MessageType.DEVICE_CONNECTED))
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (characteristic.uuid == BATTERY_LEVEL_CHARACTERISTIC_UUID) {
                postBatteryLevelChangedMessage(characteristic)
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {}

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            if (characteristic.uuid == SPECTRUM_CHARACTERISTIC_UUID) {
                val textValue = String(characteristic.value, StandardCharsets.US_ASCII)
                dataContainer.add(textValue)
            } else if (characteristic.uuid == BATTERY_LEVEL_CHARACTERISTIC_UUID) {
                postBatteryLevelChangedMessage(characteristic)
            }
        }

        private fun postBatteryLevelChangedMessage(characteristic: BluetoothGattCharacteristic) {
            val percent = String(characteristic.value)
            Log.i(TAG, "battery level changed: " + String(characteristic.value))
            EventBus.getDefault().post(MessageEvent(percent.toInt(), MessageType.BATTERY_LEVEL_CHANGED))
        }
    }

}