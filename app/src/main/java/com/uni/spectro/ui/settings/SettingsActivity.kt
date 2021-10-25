package com.uni.spectro.ui.settings

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.uni.spectro.R
import com.uni.spectro.bluetooth.BLEService
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.preferences.GlobalSettings
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.function.Function

class SettingsActivity : AppCompatActivity(), SettingsView {

    private lateinit var batteryLevel: ImageView
    private lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initFields()
    }

    private fun initFields() {
        batteryLevel = findViewById(R.id.image_battery_level_settings)
        presenter = SettingsPresenter(this, SettingsModel(loadSwitchesFromGlobalSettings()))
        presenter.setSwitchListeners()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        BLEService.instance.readBattery()
    }

    override fun onPause() {
        super.onPause()
        presenter.saveSwitchStates()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadSwitchStateFromPreferences()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.saveSwitchStates()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        presenter.handleMessageEvent(event)
    }

    private fun loadSwitchesFromGlobalSettings(): Map<GlobalSettings, SwitchMaterial> {
        return GlobalSettings.values().associateWith(convertSettingToSwitch::apply)
    }

    private val convertSettingToSwitch = Function { setting: GlobalSettings ->
        findViewById<SwitchMaterial>(resources.getIdentifier(setting.identifier(), "id", this.packageName))
    }

    override fun updateBatteryLevel(level: Int) {
        batteryLevel.setImageLevel(level)
    }

}
