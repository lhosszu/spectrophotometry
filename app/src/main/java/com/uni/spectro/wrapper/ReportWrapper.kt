package com.uni.spectro.wrapper

/**
 * Wrapping the 2 parts of a linear interpolation (calibration) report:
 * - R^2 value
 * - report itself with individual concentration data
 */
class ReportWrapper {

    lateinit var rSquared: String
    lateinit var concentrations: String

    constructor(rSquared: String, concentrations: String) {
        this.rSquared = rSquared
        this.concentrations = concentrations
    }

    constructor()

}