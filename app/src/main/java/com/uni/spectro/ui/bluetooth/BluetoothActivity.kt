package com.uni.spectro.ui.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uni.spectro.R
import com.uni.spectro.bluetooth.BLEService
import com.uni.spectro.bluetooth.BleScanner
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.root.BaseActivity
import com.uni.spectro.ui.adapters.BluetoothDeviceListAdapter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference
import java.util.*

class BluetoothActivity : BaseActivity(), BluetoothDeviceListAdapter.ItemClickListener, BluetoothView {

    private val enableBluetoothRequest: Int = 2
    private val enableLocationRequest: Int = 3

    private lateinit var bluetoothDeviceListAdapter: BluetoothDeviceListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var batteryLevel: ImageView
    private lateinit var presenter: BluetoothPresenter
    private lateinit var bluetoothScanner: BleScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        initFields()
        initRecyclerView()
        promptEnableBluetooth()
        promptEnableLocationAccess()
        presenter.autoConnect(WeakReference(this))
    }

    private fun initFields() {
        presenter = BluetoothPresenter(this)
        batteryLevel = findViewById(R.id.image_battery_level_bluetooth_page)
        progressBar = findViewById(R.id.progress_bar_bluetooth_page)
        progressBar.visibility = View.INVISIBLE
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_bluetooth_page)
        bluetoothDeviceListAdapter = BluetoothDeviceListAdapter(this, ArrayList())
        bluetoothDeviceListAdapter.setClickListener(this)
        recyclerView.adapter = bluetoothDeviceListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        BLEService.instance.readBattery()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        bluetoothScanner.stop()
    }

    override fun onItemClick(view: View?, position: Int) {
        val selectedDevice: BluetoothDevice = bluetoothDeviceListAdapter.getItem(position)
        presenter.connect(selectedDevice, WeakReference(this))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        presenter.handleMessageEvent(event, WeakReference(this))
    }

    override fun updateBatteryLevel(level: Int) {
        batteryLevel.setImageLevel(level)
    }

    override fun promptEnableBluetooth() {
        bluetoothScanner = BleScanner(bluetoothDeviceListAdapter)
        if (!presenter.btEnabled()) {
            startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), enableBluetoothRequest)
        } else {
            bluetoothDeviceListAdapter.addAll(BluetoothAdapter.getDefaultAdapter().bondedDevices)
            bluetoothScanner.start()
        }
    }

    override fun promptEnableLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), enableLocationRequest);
        }
    }

    override fun successfulDeviceConnection() {
        batteryLevel.visibility = View.VISIBLE
        Toast.makeText(this, R.string.toast_device_connected, Toast.LENGTH_SHORT).show()
        bluetoothScanner.stop()
    }

    override fun deviceDisconnected() {
        batteryLevel.visibility = View.INVISIBLE
        Toast.makeText(this, R.string.toast_device_disconnected, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            enableBluetoothRequest -> {
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothDeviceListAdapter.addAll(BluetoothAdapter.getDefaultAdapter().bondedDevices)
                    bluetoothScanner.start()
                }
            }
        }
    }

}