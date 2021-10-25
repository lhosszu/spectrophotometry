package com.uni.spectro.domain.math

import org.apache.commons.math3.stat.regression.SimpleRegression

/**
 * This class takes two arrays of the same size (x and y coordinates), and fits a linear curve.
 * Y values without a corresponding X can be calculated based on the interpolated curve.
 *
 * In the context of analytical calibration:
 *  X = concentration [mg/L]
 *  Y = absorbance [-]
 */
class LinearInterpolation(x: DoubleArray, y: DoubleArray) {

    private val slope: Double
    private val intercept: Double
    private val rSquared: Double

    init {
        val coefficients: DoubleArray = fit(x, y)
        this.intercept = coefficients[0]
        this.slope = coefficients[1]
        this.rSquared = coefficients[2]
    }

    fun xValueFor(yValue: Double): String {
        val numericConcentration = toX(yValue)
        return "%.1f".format(numericConcentration)
    }

    private fun toX(y: Double): Double {
        return (y - intercept) / slope
    }

    private fun fit(x: DoubleArray, y: DoubleArray): DoubleArray {
        val regression = SimpleRegression()
        x.zip(y).forEach { regression.addData(it.first, it.second) }
        return doubleArrayOf(regression.intercept, regression.slope, regression.rSquare)
    }

    fun rSquared(): String {
        return "R-squared: " + "%.4f".format(rSquared)
    }

}