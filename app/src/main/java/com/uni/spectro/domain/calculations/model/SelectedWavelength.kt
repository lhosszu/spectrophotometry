package com.uni.spectro.domain.calculations.model

/**
 * This class is used for wrapping a 'selected wavelength', which can be:
 * - a wavelength (and corresponding light intensity) manually selected by the user
 * - the wavelength of the global intensity maximum (selected by the application)
 */
class SelectedWavelength(private val wavelength: Int, private val value: Double) {

    fun wavelength(): Int {
        return wavelength
    }

    fun intensity(): Double {
        return value
    }

}