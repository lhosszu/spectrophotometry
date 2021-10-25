package com.uni.spectro.ui.plot

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androidplot.ui.Anchor
import com.androidplot.ui.HorizontalPositioning
import com.androidplot.ui.VerticalPositioning
import com.androidplot.xy.*
import com.uni.spectro.R
import com.uni.spectro.constants.SpectrumConstants
import com.uni.spectro.domain.calculations.SensorCalibration
import com.uni.spectro.wrapper.ExperimentWrapper
import com.uni.spectro.wrapper.JsonConverter
import java.text.DecimalFormat
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition

class PlotActivity : AppCompatActivity() {

    private val sensorCalibration: SensorCalibration = SensorCalibration()

    @SuppressLint("DefaultLocale")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plot)

        val plot = findViewById<XYPlot>(R.id.plot)
        val maxPointInfo = findViewById<TextView>(R.id.text_maximum_info)
        val experiment = fromIntent

        try {
            findViewById<TextView>(R.id.text_experiment_name_on_plot).text = experiment.name

            if (experiment.selectedWavelength != 0) {
                maxPointInfo.text = String.format("max wavelength: %d\nmax %s: %.4f", experiment.selectedWavelength, experiment.experimentType!!.toLowerCase(), experiment.intensity)
            }

            val data = experiment.points
            var series: XYSeries? = null

            if (experiment.points!!.size == SpectrumConstants.VISIBLE_RANGE_SIZE) {
                series = SimpleXYSeries(sensorCalibration.visibleRange(), data, "")
            } else if (data!!.size == SpectrumConstants.PIXEL_COUNT) {
                series = SimpleXYSeries(sensorCalibration.fullRange(), data, "")
            } else {
                Log.d(TAG, "Cannot plot NULL series")
                finish()
            }

            val lightGreen = Color.rgb(218, 227, 229)
            val format = LineAndPointFormatter(lightGreen, null, lightGreen, null)
            format.interpolationParams = CatmullRomInterpolator.Params(70, CatmullRomInterpolator.Type.Centripetal)
            format.isLegendIconEnabled = false

            plot.graph.domainGridLinePaint.color = Color.WHITE
            plot.graph.rangeGridLinePaint.color = Color.WHITE

            plot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = object : Format() {
                override fun format(obj: Any, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
                    return StringBuffer("")
                }

                override fun parseObject(source: String, pos: ParsePosition): Any? {
                    return null
                }
            }

            plot.setDomainLabel("wavelength [nm]")
            plot.setRangeLabel("intensity [-]")
            plot.domainStepValue = 8.0
            plot.rangeStepValue = 0.0
            plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("#")
            plot.domainTitle.position(0f, HorizontalPositioning.ABSOLUTE_FROM_CENTER, 50f, VerticalPositioning.ABSOLUTE_FROM_BOTTOM, Anchor.BOTTOM_MIDDLE)
            plot.rangeTitle.position(50f, HorizontalPositioning.ABSOLUTE_FROM_LEFT, 0f, VerticalPositioning.RELATIVE_TO_CENTER, Anchor.LEFT_MIDDLE)
            plot.addSeries(series, format)
            plot.legend.isVisible = false
        } catch (e: Exception) {
            e.printStackTrace()
            super.finish()
        }
    }

    private val fromIntent: ExperimentWrapper
        get() {
            val experimentJson = intent.getStringExtra("VISUALIZE_EXPERIMENT_STRING")
            return JsonConverter().toExperiment(experimentJson)
        }

    companion object {
        private val TAG = PlotActivity::class.java.name
    }
}