package com.uni.spectro.persistence

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uni.spectro.persistence.model.RealmExperiment
import io.realm.RealmConfiguration
import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class RealmPersistenceIntegrationTest {

    private val experimentName: String = "test"

    @Test
    fun canInsertExperiment() {
        // given
        val experiment = RealmExperiment(experimentName, RealmList(1.0, 2.0, 3.0))

        // when
        persistence.insert(experiment)
    }

    @Test
    fun canInsertExperimentWithCallback() {
        // given
        val latch = CountDownLatch(1)

        // when
        persistence.insert(RealmExperiment(experimentName, RealmList())) { latch.countDown() }

        // then
        assertThat(latch.count).isEqualTo(1)
    }

    @Test
    fun canQueryExperiment() {
        // given
        val experiment = RealmExperiment(experimentName, RealmList())
        persistence.insert(experiment)

        // when
        val actual = persistence.query(experimentName)

        // then
        assertThat(actual).isNotNull
    }

    @Test
    fun canReturnEmptyArrayForMissingExperiment() {
        // given
        val experiment = RealmExperiment(experimentName, RealmList())
        persistence.insert(experiment)

        // when
        val actual = persistence.query("lorem ipsum")

        // then
        assertThat(actual).isEmpty()
    }

    @Test
    fun canQueryAllExperiments() {
        // given
        persistence.insert(RealmExperiment("1", RealmList()))
        persistence.insert(RealmExperiment("2", RealmList()))

        // when
        val actual = persistence.queryAll()

        // then
        assertThat(actual).isNotNull
    }

    companion object {
        private val testConfig: RealmConfiguration = RealmConfiguration.Builder().inMemory().name("test-realm").build()
        private val persistence: RealmPersistence = RealmPersistence(Executors.newSingleThreadExecutor(), testConfig)
    }
}