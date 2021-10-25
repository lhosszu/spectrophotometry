package com.uni.spectro.messaging

import com.uni.spectro.domain.pipeline.steps.PipelineStepTestBase
import com.uni.spectro.wrapper.JsonConverter
import com.uni.spectro.persistence.model.RealmExperiment
import io.realm.RealmList
import org.assertj.core.api.Assertions
import org.junit.Test

class JsonConverterTest : PipelineStepTestBase() {
    @Test
    fun canConvertToJSON() {
        // given
        val converter = JsonConverter()

        // when
        val experiment = converter.toJson(RealmExperiment("test", RealmList()))

        // then
        Assertions.assertThat(experiment).isNotEmpty
    }

    @Test
    fun canConvertToExperiment() {
        // given
        val converter = JsonConverter()

        // when
        val experiment = converter.toExperiment("{\"name\":\"test\",\"date\":1629303090149,\"selectedWavelength\":0,\"intensity\":0}")

        // then
        Assertions.assertThat(experiment).extracting("name").isEqualTo("test")
    }
}