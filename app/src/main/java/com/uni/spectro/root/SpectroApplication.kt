package com.uni.spectro.root

import android.app.Application
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.preferences.PreferenceManager
import io.realm.Realm
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class SpectroApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.instance.initialize(this)
        Realm.init(this)
        //Realm.deleteRealm(RealmConfigurationHolder.config())
    }

    companion object {
        private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()

        private val SPECTRUM_STACK = Stack<PixelData>()

        private val EXECUTOR = ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES,
                1,
                TimeUnit.SECONDS,
                LinkedBlockingQueue()
        )

        fun executor(): ThreadPoolExecutor {
            return EXECUTOR
        }

        fun spectrumStack(): Stack<PixelData> {
            return SPECTRUM_STACK
        }
    }
}