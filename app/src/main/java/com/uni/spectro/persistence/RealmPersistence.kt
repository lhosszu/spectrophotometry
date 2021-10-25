package com.uni.spectro.persistence

import android.util.Log
import com.uni.spectro.persistence.model.RealmExperiment
import com.uni.spectro.persistence.util.RealmConfigurationHolder.Companion.config
import com.uni.spectro.root.SpectroApplication
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class RealmPersistence {

    private val executor: Executor
    private val configuration: RealmConfiguration

    constructor() {
        executor = SpectroApplication.executor()
        configuration = config()
    }

    constructor(executor: Executor, configuration: RealmConfiguration) {
        this.executor = executor
        this.configuration = configuration
    }

    fun insert(experiment: RealmExperiment) {
        CompletableFuture.runAsync(ExperimentInsert(experiment), executor)
    }

    fun insert(experiment: RealmExperiment, onInsert: Runnable) {
        CompletableFuture.runAsync(ExperimentInsert(experiment), executor).thenRun(onInsert)
    }

    fun query(name: String): DoubleArray {
        try {
            Realm.getInstance(configuration).use { realm ->
                val result: RealmExperiment? = realm.where(RealmExperiment::class.java).equalTo("name", name).findFirst()
                return result!!.dataPoints.toDoubleArray()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not query experiment with name: $name")
            return doubleArrayOf()
        }
    }

    fun queryAll(): RealmResults<RealmExperiment> {
        Realm.getInstance(configuration).use { realm ->
            return realm.where(RealmExperiment::class.java).findAll()
        }
    }

    private inner class ExperimentInsert(private val experiment: RealmExperiment) : Runnable {
        override fun run() {
            Realm.getInstance(configuration).use { realm ->
                realm.executeTransaction { transactionRealm: Realm ->
                    Log.v(TAG, "Inserting $experiment")
                    transactionRealm.insert(experiment)
                    Log.v(TAG, "Experiment inserted")
                }
            }
        }
    }

    private companion object {
        private val TAG = RealmPersistence::class.java.name
    }
}