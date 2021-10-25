package com.uni.spectro.domain.calculations.model

class SelectedWavelength(private val wavelength: Int, private val value: Double) {

    fun wavelength(): Int {
        return wavelength
    }

    fun intensity(): Double {
        return value
    }

}