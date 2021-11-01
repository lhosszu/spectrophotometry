package com.uni.spectro.ui.experiments

import android.util.Log
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.bus.MessageType
import com.uni.spectro.domain.calculations.BatteryLevel
import com.uni.spectro.domain.math.AnalyticalCalibration
import com.uni.spectro.persistence.model.RealmExperiment
import com.uni.spectro.persistence.util.RealmConfigurationHolder
import com.uni.spectro.wrapper.JsonConverter
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

class ExperimentsPresenter() {

    private lateinit var realm: Realm
    private lateinit var view: ExperimentsView

    constructor(view: ExperimentsView) : this() {
        this.view = view
        this.realm = Realm.getInstance(RealmConfigurationHolder.config())
    }

    fun findAllExperiments(): RealmResults<RealmExperiment> {
        return realm.where(RealmExperiment::class.java).findAll()
    }

    fun destroy() {
        realm.close()
    }

    fun handleMessageEvent(event: MessageEvent) {
        if (event.messageType == MessageType.BATTERY_LEVEL_CHANGED) {
            Log.i(TAG, "new battery level: " + event.content)
            setBatteryLevel(event.content)
        }
    }

    private fun setBatteryLevel(level: Int) {
        val scaled = BatteryLevel(level).level()
        view.updateBatteryLevel(scaled)
    }

    fun concentrationReport(experiments: List<RealmExperiment>, locale: Locale) {
        if (experiments.size > 3) {
            val knownExperimentCount = experiments.filter { it.concentration != 0.0 }.count()
            if (knownExperimentCount > 2) {
                val reportWrapper = AnalyticalCalibration(experiments).report(locale)
                val jsonReport = JsonConverter().toJson(reportWrapper)
                view.showReport(jsonReport)
            }
        }
    }

    private companion object {
        private val TAG = ExperimentsPresenter::class.java.name
    }

}