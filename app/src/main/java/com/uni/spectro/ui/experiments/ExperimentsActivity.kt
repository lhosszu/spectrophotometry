package com.uni.spectro.ui.experiments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.uni.spectro.R
import com.uni.spectro.bluetooth.BLEService
import com.uni.spectro.bus.MessageEvent
import com.uni.spectro.wrapper.JsonConverter
import com.uni.spectro.persistence.model.ExperimentType
import com.uni.spectro.persistence.model.RealmExperiment
import com.uni.spectro.ui.adapters.RealmExperimentListAdapter
import com.uni.spectro.ui.experiments.report.ReportActivity
import com.uni.spectro.ui.plot.CalibrationActivity
import com.uni.spectro.ui.plot.PlotActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ExperimentsActivity : AppCompatActivity(), RealmExperimentListAdapter.ItemClickListener, RealmExperimentListAdapter.ItemLongClickListener, ExperimentsView {

    private lateinit var listAdapter: RealmExperimentListAdapter
    private lateinit var batteryLevel: ImageView
    private lateinit var experimentsPresenter: ExperimentsPresenter
    private var selectedExperiments: MutableList<RealmExperiment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experiments)
        initFields()
    }

    private fun initFields() {
        batteryLevel = findViewById(R.id.image_battery_level_experiments)
        batteryLevel.setImageLevel(5)
        experimentsPresenter = ExperimentsPresenter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.view_recycler_experiment_list)

        listAdapter = RealmExperimentListAdapter(this, experimentsPresenter.findAllExperiments())
        listAdapter.setClickListener(this)
        listAdapter.setLongClickListener(this)

        recyclerView.adapter = listAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        initButton()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        BLEService.instance.readBattery()
    }

    override fun onPause() {
        super.onPause()
        selectedExperiments.clear()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        selectedExperiments.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        experimentsPresenter.destroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        experimentsPresenter.handleMessageEvent(event)
    }

    override fun onItemClick(view: View?, position: Int) {
        val selectedItem = listAdapter.getItem(position)
        if (selectedItem.experimentType == ExperimentType.CALIBRATION.name) {
            plotCalibrationDetails(selectedItem)
        } else {
            plotSpectrum(selectedItem)
        }
    }

    override fun onItemLongClick(view: View?, position: Int): Boolean {
        val selectedItem = listAdapter.getItem(position)
        if (selectedItem.experimentType == ExperimentType.CALIBRATION.name) {
            selectedExperiments.add(selectedItem)
            findViewById<MaterialToolbar>(R.id.material_toolbar_experiments_page).title = "report"
            return true
        }
        return false
    }

    override fun plotSpectrum(experiment: RealmExperiment) {
        val json = JsonConverter().toJson(experiment)
        val intent = Intent(this, PlotActivity::class.java)
        intent.putExtra("VISUALIZE_EXPERIMENT_STRING", json)
        startActivity(intent)
    }

    override fun plotCalibrationDetails(experiment: RealmExperiment) {
        val json = JsonConverter().toJson(experiment)
        val intent = Intent(this, CalibrationActivity::class.java)
        intent.putExtra("VISUALIZE_EXPERIMENT_STRING", json)
        startActivity(intent)
    }

    override fun updateBatteryLevel(level: Int) {
        batteryLevel.setImageLevel(level)
    }

    override fun showReport(report: String) {
        val intent = Intent(this, ReportActivity::class.java)
        intent.putExtra("VISUALIZE_REPORT", report)
        startActivity(intent)
    }

    private fun initButton() {
        findViewById<MaterialToolbar>(R.id.material_toolbar_experiments_page).setOnClickListener {
            experimentsPresenter.concentrationReport(selectedExperiments)
        }
    }

}