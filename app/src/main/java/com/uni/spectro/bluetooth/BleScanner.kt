package com.uni.spectro.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.util.Log
import com.uni.spectro.ui.adapters.BluetoothDeviceListAdapter

class BleScanner(private val deviceList: BluetoothDeviceListAdapter) {

    private var isScanning: Boolean = false

    fun start() {
        if (!isScanning) {
            Log.i(TAG, "Starting BLE scan")
            bleScanner.startScan(null, scanSettings, scanCallback)
            isScanning = true
        }
    }

    fun stop() {
        if (isScanning) {
            Log.i(TAG, "Stopping BLE scan")
            bleScanner.stopScan(scanCallback)
            isScanning = false
        }
    }

    private val bleScanner by lazy {
        BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.i(TAG, "Device found: ${result.device}")
            deviceList.add(result.device)
            deviceList.notifyDataSetChanged()
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e(TAG, "Scanning failed with code $errorCode")
            isScanning = false
        }
    }

    private companion object {
        private val TAG = BleScanner::class.java.name
    }

}