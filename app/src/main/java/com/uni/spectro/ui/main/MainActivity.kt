package com.uni.spectro.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.uni.spectro.R
import com.uni.spectro.bluetooth.Connect
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.ui.acquisition.AcquisitionActivity
import com.uni.spectro.ui.bluetooth.BluetoothActivity
import com.uni.spectro.ui.experiments.ExperimentsActivity
import com.uni.spectro.ui.settings.SettingsActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var batteryLevel: ImageView
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)
        initButtons()
        initBatteryLevel()
        Connect().autoConnect(WeakReference(this))
    }

    private fun initButtons() {
        findViewById<ImageButton>(R.id.btn_bluetooth).setOnClickListener { navigateToBluetoothPage() }
        findViewById<ImageButton>(R.id.btn_acquisition).setOnClickListener { navigateToAcquisitionPage() }
        findViewById<ImageButton>(R.id.btn_experiments).setOnClickListener { navigateToExperimentsPage() }
        findViewById<ImageButton>(R.id.btn_settings).setOnClickListener { navigateToSettingsPage() }
    }

    private fun initBatteryLevel() {
        batteryLevel = findViewById(R.id.image_battery_level_main)
        batteryLevel.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        presenter.handleMessageEvent(event, WeakReference(this))
    }

    override fun navigateToBluetoothPage() {
        startActivity(Intent(this, BluetoothActivity::class.java))
    }

    override fun navigateToAcquisitionPage() {
        startActivity(Intent(this, AcquisitionActivity::class.java))
    }

    override fun navigateToExperimentsPage() {
        startActivity(Intent(this, ExperimentsActivity::class.java))
    }

    override fun navigateToSettingsPage() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun updateBatteryLevel(level: Int) {
        batteryLevel.setImageLevel(level)
    }

    override fun successfulDeviceConnection() {
        batteryLevel.visibility = View.VISIBLE
        Toast.makeText(this, R.string.toast_device_connected, Toast.LENGTH_SHORT).show()
    }

    override fun deviceDisconnected() {
        batteryLevel.visibility = View.INVISIBLE
        Toast.makeText(this, R.string.toast_device_disconnected, Toast.LENGTH_SHORT).show()
    }
}