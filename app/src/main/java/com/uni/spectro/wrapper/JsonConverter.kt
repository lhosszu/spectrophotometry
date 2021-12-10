package com.uni.spectro.wrapper

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.uni.spectro.persistence.model.RealmExperiment

class JsonConverter {

    private val mapper = ObjectMapper()

    fun toJson(experiment: RealmExperiment): String {
        return try {
            val wrapper = ExperimentWrapper(experiment)
            mapper.writeValueAsString(wrapper)
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Cannot convert object to json", e)
        }
    }

    fun toJson(experiments: List<RealmExperiment>): String {
        return try {
            val wrappedExperiments: List<ExperimentWrapper> = experiments.map { ExperimentWrapper() }
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrappedExperiments)
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