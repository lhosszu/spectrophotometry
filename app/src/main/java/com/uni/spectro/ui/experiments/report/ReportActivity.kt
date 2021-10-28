package com.uni.spectro.ui.experiments.report

import android.os.Bundle
import android.widget.TextView
import com.uni.spectro.R
import com.uni.spectro.root.BaseActivity
import com.uni.spectro.wrapper.JsonConverter
import com.uni.spectro.wrapper.ReportWrapper

class ReportActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        // intent receives object serialized to JSON string
        val jsonReport: String? = intent.getStringExtra("VISUALIZE_REPORT")
        val reportWrapper: ReportWrapper = JsonConverter().toReport(jsonReport!!)

        findViewById<TextView>(R.id.text_r_squared).text = reportWrapper.rSquared
        findViewById<TextView>(R.id.text_report).text = reportWrapper.concentrations
    }

}