package com.uni.spectro.domain.calculations

import org.assertj.core.api.Assertions
import org.junit.Test

class AbsorbanceCalculatorTest {

    @Test
    fun canCalculateAbsorbanceOfIdealBackgroundAndSignal() {
        // given
        val signal = doubleArrayOf(2.0, 1.0, 6.0, 3.0, 5.0)

        // when
        val calculator = AbsorbanceCalculator(BACKGROUND, signal, 5)
        val actual = calculator.absorbance()

        // then
        Assertions.assertThat(actual).containsExactly(0.69, 1.0, 0.22, 0.52, 0.3)
    }

    // zero signal = all absorbed (should never be zero because of the noise)
    @Test
    fun canCalculateAbsorbanceBackgroundAndZeroSignal() {
        // given
        val signal = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0)

        // when
        val calculator = AbsorbanceCalculator(BACKGROUND, signal, 5)
        val actual = calculator.absorbance()

        // then
        Assertions.assertThat(actual).containsExactly(1.0, 1.0, 1.0, 1.0, 1.0)
    }

    // in theory the signal is always lower than (or equal to) the background
    @Test
    fun canCalculateAbsorbanceOfBackgroundAndBiggerSignal() {
        // given
        val signal = doubleArrayOf(11.0, 11.0, 11.0, 11.0, 11.0)

        // when
        val calculator = AbsorbanceCalculator(BACKGROUND, signal, 5)
        val actual = calculator.absorbance()

        // then
        Assertions.assertThat(actual).containsExactly(0.0, 0.0, 0.0, 0.0, 0.0)
    }

    companion object {
        private val BACKGROUND = doubleArrayOf(10.0, 10.0, 10.0, 10.0, 10.0)
    }
}