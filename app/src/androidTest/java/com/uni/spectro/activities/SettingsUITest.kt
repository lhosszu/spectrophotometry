package com.uni.spectro.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uni.spectro.R
import com.uni.spectro.preferences.GlobalSettings
import com.uni.spectro.preferences.PreferenceManager
import com.uni.spectro.ui.settings.SettingsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SettingsUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SettingsActivity::class.java)

    @Test
    fun canDisplayActionbar() {
        onView(withId(R.id.toolbar_settings_page)).check(matches(isDisplayed()))
    }

    @Test
    fun canDisplayBatteryLevel() {
        onView(withId(R.id.image_battery_level_settings)).check(matches(isDisplayed()))
    }

    @Test
    fun canSwitchAutoConnect() {
        // given
        val before: Boolean = PreferenceManager.instance.getPreference(GlobalSettings.AUTO_CONNECT)

        // when
        onView(withId(R.id.switch_auto_connect)).check(matches(isClickable())).perform(click())
        val after: Boolean = PreferenceManager.instance.getPreference(GlobalSettings.AUTO_CONNECT)

        // then
        assert(before == !after)
    }

    @Test
    fun canSwitchSmoothSpectrum() {
        // given
        val before: Boolean = PreferenceManager.instance.getPreference(GlobalSettings.SMOOTH_SPECTRUM)

        // when
        onView(withId(R.id.switch_smooth)).check(matches(isClickable())).perform(click())
        val after: Boolean = PreferenceManager.instance.getPreference(GlobalSettings.SMOOTH_SPECTRUM)

        // then
        assert(before == !after)
    }

    @Test
    fun canSwitchAnalyzePeaks() {
        // given
        val before: Boolean = PreferenceManager.instance.getPreference(GlobalSettings.ANALYZE_PEAKS)

        // when
        onView(withId(R.id.switch_analyze)).check(matches(isClickable())).perform(click())
        val after: Boolean = PreferenceManager.instance.getPreference(GlobalSettings.ANALYZE_PEAKS)

        // then
        assert(before == !after)
    }

    @Test
    fun canSwitchAutoVisualization() {
        // given
        val before: Boolean = PreferenceManager.instance.getPreference(GlobalSettings.OPEN_AFTER_ACQUISITION)

        // when
        onView(withId(R.id.switch_open_after)).check(matches(isClickable())).perform(click())
        val after: Boolean = PreferenceManager.instance.getPreference(GlobalSettings.OPEN_AFTER_ACQUISITION)

        // then
        assert(before == !after)
    }

    @Test
    fun canSwitchWavelengthSelection() {
        // given
        val before: Boolean = PreferenceManager.instance.getPreference(GlobalSettings.SELECTED_WAVELENGTH)

        // when
        onView(withId(R.id.switch_selected_wavelength)).check(matches(isClickable())).perform(click())
        val after: Boolean = PreferenceManager.instance.getPreference(GlobalSettings.SELECTED_WAVELENGTH)

        // then
        assert(before == !after)
    }
}