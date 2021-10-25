package com.uni.spectro.root

import android.app.Application
import android.content.Intent
import com.uni.spectro.domain.pipeline.model.PixelData
import com.uni.spectro.preferences.PreferenceManager
import com.uni.spectro.services.BatteryLevelService
import io.realm.Realm
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class SpectroApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startService(Intent(this, BatteryLevelService::class.java))
        PreferenceManager.instance.initialize(this)
        Realm.init(this)
    }

    companion object {
        private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()

        fun executor(): ThreadPoolExecutor {
            return ThreadPoolExecutor(
                    NUMBER_OF_CORES,
                    NUMBER_OF_CORES,
                    1,
                    TimeUnit.SECONDS,
                    LinkedBlockingQueue()
            )
        }

        fun spectrumStack(): Stack<PixelData> {
            return Stack<PixelData>()
        }
    }
}