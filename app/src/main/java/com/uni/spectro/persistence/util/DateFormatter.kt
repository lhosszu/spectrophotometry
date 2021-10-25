package com.uni.spectro.persistence.util

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {
    companion object {
        private val format = SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
        fun format(date: Date): String {
            return format.format(date)
        }
    }
}