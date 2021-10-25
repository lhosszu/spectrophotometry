package com.uni.spectro.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uni.spectro.R
import com.uni.spectro.ui.acquisition.AcquisitionActivity
import com.uni.spectro.ui.bluetooth.BluetoothActivity
import com.uni.spectro.ui.experiments.ExperimentsActivity
import com.uni.spectro.ui.main.MainActivity
import com.uni.spectro.ui.settings.SettingsActivity
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

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
        onView(withId(R.id.toolbar_main_page)).check(matches(isDisplayed()))
    }

    @Test
    fun canDisplayBluetoothButton() {
        onView(withId(R.id.btn_bluetooth)).check(matches(isDisplayed()))
    }

    @Test
    fun canDisplayAcquisitionButton() {
        onView(withId(R.id.btn_acquisition)).check(matches(isDisplayed()))
    }

    @Test
    fun canDisplayExperimentsButton() {
        onView(withId(R.id.btn_experiments)).check(matches(isDisplayed()))
    }

    @Test
    fun canDisplaySettingsButton() {
        onView(withId(R.id.btn_settings)).check(matches(isDisplayed()))
    }

    @Test
    fun canClickBluetoothButton() {
        onView(withId(R.id.btn_bluetooth)).perform(click())
        intended(hasComponent(BluetoothActivity::class.java.name))
    }

    @Test
    fun canClickAcquisitionButton() {
        onView(withId(R.id.btn_acquisition)).perform(click())
        intended(hasComponent(AcquisitionActivity::class.java.name))
    }

    @Test
    fun canClickExperimentsButton() {
        onView(withId(R.id.btn_experiments)).perform(click())
        intended(hasComponent(ExperimentsActivity::class.java.name))
    }

    @Test
    fun canClickSettingsButton() {
        onView(withId(R.id.btn_settings)).perform(click())
        intended(hasComponent(SettingsActivity::class.java.name))
    }

}