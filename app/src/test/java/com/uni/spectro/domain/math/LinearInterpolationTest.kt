package com.uni.spectro.domain.math

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class LinearInterpolationTest {

    @Test
    fun whenInterpolateSimpleLinearCurve_thenCalculateValueInRange() {
        // given
        val interpolation = LinearInterpolation(doubleArrayOf(0.0, 1.0, 2.0), doubleArrayOf(1.0, 2.0, 3.0))

        // when
        val concentration: String = interpolation.xValueFor(1.5)

        // then
        assertThat(concentration).isEqualTo("0.5")
        assertThat(interpolation.rSquared()).isEqualTo("R-squared: 1.0000")
    }

    @Test
    fun whenInterpolateRegularLinearCurve_thenCalculateValueInRange() {
        // given
        val interpolation = LinearInterpolation(doubleArrayOf(12.5, 10.0, 8.3, 6.3), doubleArrayOf(0.224, 0.193, 0.117, 0.098))

        // when
        val concentration: String = interpolation.xValueFor(0.111)

        // then
        assertThat(concentration).isEqualTo("7.1")
        assertThat(interpolation.rSquared()).isEqualTo("R-squared: 0.9264")
    }

}