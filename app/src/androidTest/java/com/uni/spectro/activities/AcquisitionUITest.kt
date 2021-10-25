package com.uni.spectro.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uni.spectro.R
import com.uni.spectro.ui.acquisition.AcquisitionActivity
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class AcquisitionUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AcquisitionActivity::class.java)

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUp() {
            Intents.init()
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            Intents.release()
        }
    }

    @Test
    fun canDisplayActionbar() {
        onView(withId(R.id.toolbar_acquisition_page)).check(matches(isDisplayed()))
    }

    @Test
    fun canDisplayTransmittanceButton() {
        onView(withId(R.id.btn_transmittance)).check(matches(isDisplayed()))
    }

    @Test
    fun transmittanceButtonHasCorrectName() {
        onView(withId(R.id.text_transmittance))
                .check(matches(withParent(withId(R.id.layout_transmittance))))
                .check(matches(withText("transmittance")))
    }

    @Test
    fun canClickTransmittanceButton() {
        onView(withId(R.id.btn_transmittance)).check(matches(isClickable()))
    }

    @Test
    fun canDisplayAbsorbanceButton() {
        onView(withId(R.id.btn_absorbance)).check(matches(isDisplayed()))
    }

    @Test
    fun absorbanceButtonHasCorrectName() {
        onView(withId(R.id.text_absorbance))
                .check(matches(withParent(withId(R.id.layout_absorbance))))
                .check(matches(withText("absorbance")))
    }

    @Test
    fun canClickAbsorbanceButton() {
        onView(withId(R.id.btn_absorbance)).check(matches(isClickable()))
    }

    @Test
    fun canDisplayCalibrationButton() {
        onView(withId(R.id.btn_calibration)).check(matches(isDisplayed()))
    }

    @Test
    fun calibrationButtonHasCorrectName() {
        onView(withId(R.id.text_calibration))
                .check(matches(withParent(withId(R.id.layout_calibration))))
                .check(matches(withText("calibration")))
    }

    @Test
    fun canClickCalibrationButton() {
        onView(withId(R.id.btn_calibration)).check(matches(isClickable()))
    }

    @Test
    fun canDisplayConcentrationInput() {
        onView(withId(R.id.text_concentration)).check(matches(isDisplayed()))
    }

    @Test
    fun canDisplayExperimentNameInput() {
        onView(withId(R.id.text_experiment_name)).check(matches(isDisplayed()))
    }
}