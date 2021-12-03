package com.uni.spectro.domain.pipeline.steps.filter.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class GaussianBlurTest {

    private val input = doubleArrayOf(1000.0, 2000.0, 3000.0, 2000.0, 1000.0, 1000.0, 3000.0, 2000.0, 5000.0, 6000.0)

    @Test
    fun whenRunGaussianBlur_thenFilterSpectrum() {
        // when
        val result = GaussianBlur().filter(input)

        // then
        assertThat(result).containsExactly(1000.0, 2000.0, 3000.0, 1945.8, 1466.7, 1618.3, 2282.5, 2000.0, 5000.0, 6000.0)
    }

    @Test
    fun whenRunGaussianBlur_thenKeepLeadingAndTrailingPoints() {
        // when
        val result = GaussianBlur().filter(input)

        // then
        println(result.contentToString())
        assertThat(result).endsWith(2000.0, 5000.0, 6000.0)
        assertThat(result).startsWith(1000.0, 2000.0, 3000.0)
    }

}