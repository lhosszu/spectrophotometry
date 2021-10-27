package com.uni.spectro.constants

object SpectrumConstants {
    const val PIXEL_COUNT = 288
    const val VISIBLE_RANGE_SIZE = 193
    const val VISIBLE_RANGE_LOWER_LIMIT = 32

    const val BOX_FILTER_WINDOW_SIZE = 5
    const val BOX_FILTER_PADDING = (BOX_FILTER_WINDOW_SIZE - 1) / 2

    // constants for light intensity validation
    const val INTENSITY_TOLERANCE_PERCENT: Double = 95.0
    const val INTENSITY_SUM_LIMIT = 21145
    const val THEORETICAL_INTENSITY_MAX = 2 shl 12
}