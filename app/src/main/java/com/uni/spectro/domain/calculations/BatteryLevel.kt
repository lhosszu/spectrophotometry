package com.uni.spectro.domain.calculations

class BatteryLevel(private var percentage: Int) {

    fun level(): Int {
        if (0 > percentage || 100 < percentage) {
            return 0
        }
        return scale(percentage)
    }

    private fun scale(percent: Int): Int {
        return when {
            percent > 80 -> 5
            percent > 60 -> 4
            percent > 40 -> 3
            percent > 30 -> 2
            percent > 20 -> 1
            else -> 0
        }
    }

    companion object {
        private val TAG = BatteryLevel::class.java.name
    }
}