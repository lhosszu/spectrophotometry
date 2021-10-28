package com.uni.spectro.ui.acquisition

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.android.material.textfield.TextInputLayout
import com.uni.spectro.R
import com.uni.spectro.bluetooth.BLEService
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.root.BaseActivity
import org.apache.commons.lang3.StringUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

class AcquisitionActivity : BaseActivity(), AcquisitionView {

    private lateinit var experimentNameField: TextInputLayout
    private lateinit var concentrationField: TextInputLayout
    private lateinit var batteryLevel: ImageView
    private lateinit var presenter: AcquisitionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acquisition)
        initFields()
        setClickListeners()
    }

    private fun setClickListeners() {
        findViewById<View>(R.id.btn_transmittance).setOnClickListener { transmittanceButtonClick() }
        findViewById<View>(R.id.btn_absorbance).setOnClickListener { absorbanceButtonClick() }
        findViewById<View>(R.id.btn_calibration).setOnClickListener { calibrationButtonClick() }
    }

    private fun initFields() {
        presenter = AcquisitionPresenter(this)
        experimentNameField = findViewById(R.id.text_experiment_name)
        concentrationField = findViewById(R.id.text_concentration)
        batteryLevel = findViewById(R.id.image_battery_level_acquisition)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        BLEService.instance.readBattery()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        presenter.handleMessageEvent(event)
    }

    override fun transmittanceButtonClick() {
        presenter.collectTransmittance(WeakReference(this), experimentNameField.editText!!)
    }

    override fun absorbanceButtonClick() {
        presenter.collectAbsorbance(WeakReference(this), experimentNameField.editText!!)
    }

    override fun calibrationButtonClick() {
        presenter.collectCalibration(WeakReference(this), experimentNameField.editText!!)
    }

    override fun updateBatteryLevel(level: Int) {
        batteryLevel.setImageLevel(level)
    }

    override fun readConcentration(): Double {
        val editText = concentrationField.editText!!
        return if (StringUtils.isNumeric(editText.text)) return editText.text.toString().toDouble() else 0.0
    }
}