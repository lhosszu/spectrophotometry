package com.uni.spectro.domain.pipeline

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.persistence.RealmPersistence
import com.uni.spectro.persistence.model.RealmExperiment
import io.realm.RealmConfiguration
import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class SignalProcessingPipelineIntegrationTest {

    private val pipeline: SignalProcessingPipeline = SignalProcessingPipeline()
    private val experimentName: String = "background"

    @Test
    fun canRunQueryPipeline() {
        // given
        insertBackground()

        // when
        val result: PixelData = pipeline.queryPipeline(experimentName)

        // then
        assertThat(result.pixelData()).hasSize(2)
    }

    private fun insertBackground() {
        persistence.insert(RealmExperiment(experimentName, RealmList(1.0, 2.0)))
    }

    companion object {
        private val testConfig: RealmConfiguration = RealmConfiguration.Builder().inMemory().name("test-realm").build()
        private val persistence: RealmPersistence = RealmPersistence(Executors.newSingleThreadExecutor(), testConfig)
    }
}