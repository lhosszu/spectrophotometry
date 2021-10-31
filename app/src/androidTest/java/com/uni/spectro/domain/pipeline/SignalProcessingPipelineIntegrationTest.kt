package com.uni.spectro.domain.pipeline

import android.util.Pair
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.uni.spectro.constants.SpectrumConstants
import com.uni.spectro.domain.pipeline.model.ExperimentDetails
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.domain.pipeline.model.Void
import com.uni.spectro.domain.pipeline.steps.AcquisitionStep
import com.uni.spectro.persistence.RealmPersistence
import com.uni.spectro.persistence.model.RealmExperiment
import com.uni.spectro.preferences.GlobalSettings
import com.uni.spectro.preferences.PreferenceManager
import io.realm.RealmConfiguration
import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class SignalProcessingPipelineIntegrationTest {

    private val experimentName: String = "background"

    @Test
    fun canRunTransmittanceSpectrumPipeline() {
        // given
        PreferenceManager.instance.setPreference(GlobalSettings.OPEN_AFTER_ACQUISITION, false)
        val rawData = DoubleArray(SpectrumConstants.PIXEL_COUNT).map { Random.nextDouble() }.toDoubleArray()
        val pipeline = SignalProcessingPipeline(DummyAcquisitionStep(rawData))

        // when
        val result: PixelData = pipeline.transmittanceSpectrumPipeline(transmittanceDetails(), context()).execute(Void())

        // then
        assertThat(result.pixelData()).hasSize(SpectrumConstants.VISIBLE_RANGE_SIZE)
    }

    @Test
    fun canRunDataCollectionPipeline() {
        // given
        val rawData = DoubleArray(SpectrumConstants.PIXEL_COUNT).map { Random.nextDouble() }.toDoubleArray()
        val pipeline = SignalProcessingPipeline(DummyAcquisitionStep(rawData))

        // when
        val result = pipeline.collectPipeline().execute(Void())

        // then
        assertThat(result.pixelData()).isEqualTo(rawData)
    }

    @Test
    fun canRunAbsorbancePipeline() {
        // given
        val background = DoubleArray(SpectrumConstants.PIXEL_COUNT).map { Random.nextDouble() }.toDoubleArray()
        val signal = DoubleArray(SpectrumConstants.PIXEL_COUNT).map { Random.nextDouble() }.toDoubleArray()
        val pipeline = SignalProcessingPipeline(DummyAcquisitionStep(doubleArrayOf()))

        // when
        val input: Pair<PixelData, PixelData> = Pair(PixelData(background), PixelData(signal))
        val result = pipeline.absorbanceSpectrumPipeline(absorbanceDetails(), context()).execute(input)

        // then
        assertThat(result.pixelData()).hasSize(SpectrumConstants.VISIBLE_RANGE_SIZE)
    }

    @Test
    fun canRunCalibrationPipeline() {
        // given
        val background = DoubleArray(SpectrumConstants.PIXEL_COUNT).map { Random.nextDouble() }.toDoubleArray()
        val signal = DoubleArray(SpectrumConstants.PIXEL_COUNT).map { Random.nextDouble() }.toDoubleArray()
        val pipeline = SignalProcessingPipeline(DummyAcquisitionStep(doubleArrayOf()))

        // when
        val input: Pair<PixelData, PixelData> = Pair(PixelData(background), PixelData(signal))
        val result = pipeline.calibrationPipeline(calibrationDetails()).execute(input)

        // then
        assertThat(result.pixelData()).hasSize(1)
    }

    @Test
    fun canRunBackgroundPipeline() {
        // given
        val background = DoubleArray(SpectrumConstants.PIXEL_COUNT)
        background[144] = 21145.0
        val pipeline = SignalProcessingPipeline(DummyAcquisitionStep(background))

        // when
        val result = pipeline.background(context()).execute(Void())

        // then
        assertThat(result.pixelData()).hasSize(SpectrumConstants.PIXEL_COUNT)
    }

    @Test
    fun canRunQueryPipeline() {
        // given
        val pipeline = SignalProcessingPipeline(DummyAcquisitionStep(doubleArrayOf()))
        persistence.insert(RealmExperiment(experimentName, RealmList(1.0, 2.0)))

        // when
        val result: PixelData = pipeline.queryPipeline(experimentName, persistence)

        // then
        assertThat(result.pixelData()).hasSize(2)
    }

    private fun context() = WeakReference(InstrumentationRegistry.getInstrumentation().context)

    private fun transmittanceDetails(): ExperimentDetails {
        return ExperimentDetails(experimentName, persistence)
    }

    private fun absorbanceDetails(): ExperimentDetails {
        return ExperimentDetails().absorbance("abs")
    }

    private fun calibrationDetails(): ExperimentDetails {
        return ExperimentDetails().calibration("abs", 555, 10.0)
    }

    private inner class DummyAcquisitionStep(private val rawData: DoubleArray) : AcquisitionStep() {
        override fun invoke(input: Void): PixelData {
            return PixelData(rawData)
        }
    }

    private companion object {
        private val testConfig: RealmConfiguration = RealmConfiguration.Builder().inMemory().name("test-realm").build()
        private val persistence: RealmPersistence = RealmPersistence(Executors.newSingleThreadExecutor(), testConfig)
    }
}