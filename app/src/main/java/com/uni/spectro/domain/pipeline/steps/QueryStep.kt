package com.uni.spectro.domain.pipeline.steps

import com.uni.spectro.persistence.RealmPersistence
import com.uni.spectro.domain.pipeline.Step
import com.uni.spectro.domain.pipeline.exception.PipelineInvocationException
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.domain.pipeline.model.Void

/**
 * Querying an experiment and passing the data to the next pipeline step.
 * If the target does not exist, breaks the pipeline.
 */
class QueryStep : Step<Void, PixelData> {

    private val realmPersistence: RealmPersistence
    private val query: String

    constructor(realmPersistence: RealmPersistence, query: String) {
        this.realmPersistence = realmPersistence
        this.query = query
    }

    constructor(query: String) {
        this.query = query
        realmPersistence = RealmPersistence()
    }

    override operator fun invoke(input: Void): PixelData {
        val result = realmPersistence.query(query)
        if (result.isEmpty()) {
            throw PipelineInvocationException("Cannot query experiment")
        }
        return PixelData(result)
    }
}