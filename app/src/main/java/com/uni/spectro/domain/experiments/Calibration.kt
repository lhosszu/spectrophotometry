package com.uni.spectro.domain.experiments

import android.content.Context
import android.content.DialogInterface
import android.util.Pair
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uni.spectro.R
import com.uni.spectro.domain.pipeline.SignalProcessingPipeline
import com.uni.spectro.domain.pipeline.exception.PipelineInvocationException
import com.uni.spectro.domain.pipeline.model.ExperimentDetails
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.preferences.GlobalSettings
import com.uni.spectro.preferences.PreferenceManager
import com.uni.spectro.root.SpectroApplication
import java.lang.ref.WeakReference
import java.util.concurrent.CompletableFuture

/**
 * Calibration workflow:
 * - if corresponding option is set, single wavelength is selected in a pupup window
 * - input wavelength is validated
 * - on clicking OK:
 * - query background from the DB, collect fresh data, and calculate absorbance on single wavelength
 * - on CANCEL: no processing is done
 */
class Calibration(private val context: WeakReference<Context>, private val experimentName: String, private val concentration: Double) {

    private val processingPipeline: SignalProcessingPipeline = SignalProcessingPipeline()

    fun run() {
        if (PreferenceManager.instance.getPreference(GlobalSettings.SELECTED_WAVELENGTH)) {
            collectWithDialog()
        } else {
            collectWithoutDialog()
        }
    }

    // Extract wavelength provided by user
    private fun collectWithDialog() {
        val taskEditText = EditText(context.get())
        val builder = MaterialAlertDialogBuilder(context.get()!!)
        builder.setMessage("SELECT WAVELENGTH")
                .setView(taskEditText)
                .setPositiveButton("OK") { _: DialogInterface?, _: Int ->
                    val userInput = taskEditText.text.toString()
                    CompletableFuture.runAsync({ ok(userInput) }, SpectroApplication.executor())
                }
                .setNegativeButton("CANCEL", null)
                .create()
        builder.show()
    }

    // Use wavelength with maximum value (AnalyzeStep)
    private fun collectWithoutDialog() {
        try {
            collect(0)
        } catch (e: NumberFormatException) {
            Toast.makeText(context.get(), "INPUT IS NOT A VALID WAVELENGTH", Toast.LENGTH_SHORT).show()
        } catch (e: PipelineInvocationException) {
            Toast.makeText(context.get(), "INPUT IS NOT A VALID WAVELENGTH", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ok(input: String) {
        try {
            val wavelength = convertUserInput(input)
            collect(wavelength)
        } catch (e: NumberFormatException) {
            Toast.makeText(context.get(), "INPUT IS NOT A VALID WAVELENGTH", Toast.LENGTH_SHORT).show()
        } catch (e: PipelineInvocationException) {
            Toast.makeText(context.get(), "INPUT IS NOT A VALID WAVELENGTH", Toast.LENGTH_SHORT).show()
        }
    }

    private fun collect(wavelength: Int) {
        val experiment = ExperimentDetails().calibration(experimentName, wavelength, concentration)
        val background = SignalProcessingPipeline().queryPipeline("background")
        afterBackgroundQuery(experiment, background)
    }

    private fun afterBackgroundQuery(experiment: ExperimentDetails, background: PixelData) {
        if (!background.isEmpty) {
            val sample = processingPipeline.collectPipeline()
            processingPipeline.calibrationPipeline(experiment).execute(Pair(background, sample))
        } else {
            Toast.makeText(context.get(), R.string.toast_missing_baseline, Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertUserInput(input: String): Int {
        val wavelength = input.toInt()
        if (wavelength > 800 || wavelength < 400) {
            throw PipelineInvocationException("NOT IN VISIBLE RANGE")
        }
        return wavelength
    }

}