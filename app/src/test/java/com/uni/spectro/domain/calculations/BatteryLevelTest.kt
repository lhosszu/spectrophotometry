package com.uni.spectro.domain.calculations

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BatteryLevelTest {

    @Test
    fun canConvertBatteryPercentToLevel() {
        // given
        val percent = 83

        // when
        val level = BatteryLevel(percent).level()

        // then
        assertThat(level).isEqualTo(5)
    }

    @Test
    fun canConvertLowBatteryPercentToZeroLevel() {
        // given
        val percent = 10

        // when
        val level = BatteryLevel(percent).level()

        // then
        assertThat(level).isEqualTo(0)
    }

    @Test
    fun canConvertInvalidBatteryPercentToLevel() {
        // given
        val percent = 183

        // when
        val level = BatteryLevel(percent).level()

        // then
        assertThat(level).isEqualTo(0)
    }
}