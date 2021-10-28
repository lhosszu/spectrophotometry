package com.uni.spectro.ui.plot

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.androidplot.ui.Anchor
import com.androidplot.ui.HorizontalPositioning
import com.androidplot.ui.VerticalPositioning
import com.androidplot.xy.*
import com.uni.spectro.R
import com.uni.spectro.constants.SpectrumConstants
import com.uni.spectro.domain.calculations.SensorCalibration
import com.uni.spectro.root.BaseActivity
import com.uni.spectro.wrapper.ExperimentWrapper
import com.uni.spectro.wrapper.JsonConverter
import java.text.DecimalFormat
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.util.*

class PlotActivity : BaseActivity() {

    private val spectrumMaxInfo: String = resources.getString(R.string.label_spectrum_max_info)
    private val xAxisName = resources.getString(R.string.label_x_axis);
    private val yAxisName = resources.getString(R.string.label_y_axis);

    private lateinit var plot: XYPlot
    private lateinit var maxPointInfo: TextView
    private lateinit var experimentName: TextView
    private lateinit var experiment: ExperimentWrapper

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plot)

        initFields()
        plot()
    }

    private fun initFields() {
        plot = findViewById(R.id.plot)
        maxPointInfo = findViewById(R.id.text_maximum_info)
        experimentName = findViewById(R.id.text_experiment_name_on_plot)
        experiment = fromIntent
        setTextFields()
    }

    private fun setTextFields() {
        experimentName.text = experiment.name
        if (experiment.selectedWavelength != 0) {
            maxPointInfo.text = maxPointInfo(experiment)
        }
    }

    private fun plot() {
        setPlotProperties()
        when (experiment.points.size) {
            SpectrumConstants.VISIBLE_RANGE_SIZE -> {
                val series: XYSeries = SimpleXYSeries(SensorCalibration().visibleRange(), experiment.points, "")
                plot.addSeries(series, plotFormat())
            }

            SpectrumConstants.PIXEL_COUNT -> {
                val series: XYSeries = SimpleXYSeries(SensorCalibration().fullRange(), experiment.points, "")
                plot.addSeries(series, plotFormat())
            }

            else -> {
                Log.e(TAG, "Cannot plot NULL series")
                finish()
            }
        }
    }

    private fun setPlotProperties() {
        plot.graph.domainGridLinePaint.color = Color.WHITE
        plot.graph.rangeGridLinePaint.color = Color.WHITE
        plot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = lineLabelFormat()
        plot.setDomainLabel(xAxisName)
        plot.setRangeLabel(yAxisName)
        plot.domainStepValue = 8.0
        plot.rangeStepValue = 0.0
        plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("#")
        plot.domainTitle.position(0f, HorizontalPositioning.ABSOLUTE_FROM_CENTER, 50f, VerticalPositioning.ABSOLUTE_FROM_BOTTOM, Anchor.BOTTOM_MIDDLE)
        plot.rangeTitle.position(50f, HorizontalPositioning.ABSOLUTE_FROM_LEFT, 0f, VerticalPositioning.RELATIVE_TO_CENTER, Anchor.LEFT_MIDDLE)
        plot.legend.isVisible = false
    }

    private fun plotFormat(): LineAndPointFormatter {
        val format = LineAndPointFormatter(spectrumColor(), null, spectrumColor(), null)
        format.interpolationParams = CatmullRomInterpolator.Params(70, CatmullRomInterpolator.Type.Centripetal)
        format.isLegendIconEnabled = false
        return format
    }

    private fun spectrumColor(): Int {
        return Color.rgb(218, 227, 229)
    }

    private fun maxPointInfo(experiment: ExperimentWrapper) =
            String.format(spectrumMaxInfo,
                    experiment.selectedWavelength,
                    experiment.experimentType.toLowerCase(Locale.ROOT),
                    experiment.intensity)

    private fun lineLabelFormat() = object : Format() {
        override fun format(obj: Any, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
            return StringBuffer("")
        }

        override fun parseObject(source: String, pos: ParsePosition): Any? {
            return null
        }
    }

    private val fromIntent: ExperimentWrapper
        get() {
            val experimentJson = intent.getStringExtra("VISUALIZE_EXPERIMENT_STRING")
            return JsonConverter().toExperiment(experimentJson)
        }

    private companion object {
        private val TAG = PlotActivity::class.java.name
    }
}