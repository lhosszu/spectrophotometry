package com.uni.spectro.ui.acquisition

import android.content.Context
import android.util.Log
import android.widget.EditText
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.bus.MessageType
import com.uni.spectro.domain.calculations.BatteryLevel
import com.uni.spectro.domain.experiments.Absorbance
import com.uni.spectro.domain.experiments.Calibration
import com.uni.spectro.domain.experiments.Transmittance
import com.uni.spectro.ui.experiments.ExperimentsPresenter
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import java.lang.ref.WeakReference

class AcquisitionPresenter(private val view: AcquisitionView) {

    fun handleMessageEvent(event: MessageEvent) {
        if (event.messageType == MessageType.BATTERY_LEVEL_CHANGED) {
            Log.i(TAG, "new battery level: " + event.content)
            setBatteryLevel(event.content)
        }
    }

    fun collectTransmittance(context: WeakReference<Context>, editText: EditText) {
        val experimentName = readExperimentName(editText)
        Transmittance(context, experimentName).run()
    }

    fun collectAbsorbance(context: WeakReference<Context>, editText: EditText) {
        val experimentName = readExperimentName(editText)
        Absorbance(context, experimentName).run()
    }

    fun collectCalibration(context: WeakReference<Context>, editText: EditText) {
        val experimentName = readExperimentName(editText)
        Calibration(context, experimentName).run()
    }

    private fun readExperimentName(editText: EditText): String {
        if (StringUtils.isNotBlank(editText.text)) {
            return editText.text.toString()
        }
        Log.i(TAG, "generating name for experiment")
        return RandomStringUtils.randomAlphabetic(5)
    }

    private fun setBatteryLevel(level: Int) {
        val scaled = BatteryLevel(level).level()
        view.updateBatteryLevel(scaled)
    }

    companion object {
        private val TAG = ExperimentsPresenter::class.java.name
    }
}