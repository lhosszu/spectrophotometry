package com.uni.spectro.ui.plot

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.uni.spectro.R
import com.uni.spectro.bluetooth.BLEService
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.wrapper.ExperimentWrapper
import com.uni.spectro.wrapper.JsonConverter
import com.uni.spectro.persistence.util.DateFormatter.Companion.format
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CalibrationActivity : AppCompatActivity(), CalibrationView {

    private lateinit var batteryLevel: ImageView
    private lateinit var experimentName: TextView
    private lateinit var experimentDate: TextView
    private lateinit var absorbance: TextView
    private lateinit var wavelength: TextView
    private lateinit var presenter: CalibrationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibration)
        batteryLevel = findViewById(R.id.image_calibration_battery_level)
        presenter = CalibrationPresenter(this)
        initTextViews()
        showExperimentDetails()
    }

    private fun initTextViews() {
        experimentName = findViewById(R.id.text_calibration_name_placeholder)
        experimentDate = findViewById(R.id.text_calibration_date_placeholder)
        absorbance = findViewById(R.id.text_calibration_absorbance_placeholder)
        wavelength = findViewById(R.id.text_calibration_wavelength_placeholder)
    }

    override fun showExperimentDetails() {
        val experiment = fromIntent
        experimentName.text = experiment.name
        experimentDate.text = format(experiment.date!!)
        absorbance.text = experiment.intensity.toString()
        wavelength.text = experiment.selectedWavelength.toString()
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

    override fun updateBatteryLevel(level: Int) {
        batteryLevel.setImageLevel(level)
    }

    private val fromIntent: ExperimentWrapper
        get() {
            val experimentJson = intent.getStringExtra("VISUALIZE_EXPERIMENT_STRING")
            return JsonConverter().toExperiment(experimentJson)
        }

}