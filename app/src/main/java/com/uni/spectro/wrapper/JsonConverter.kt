package com.uni.spectro.wrapper

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.uni.spectro.persistence.model.RealmExperiment

class JsonConverter {

    private val mapper = ObjectMapper()

    fun toJson(`object`: RealmExperiment): String {
        return try {
            val wrapper = ExperimentWrapper(`object`)
            mapper.writeValueAsString(wrapper)
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Cannot convert object to json", e)
        }
    }

    fun toJson(input: ReportWrapper): String {
        return try {
            mapper.writeValueAsString(input)
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Cannot convert object to json", e)
        }
    }

    fun toExperiment(json: String?): ExperimentWrapper {
        return try {
            mapper.readValue(json, ExperimentWrapper::class.java)
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Cannot convert json to object", e)
        }
    }

    fun toReport(json: String): ReportWrapper {
        return try {
            mapper.readValue(json, ReportWrapper::class.java)
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Cannot convert json to object", e)
        }
    }
}