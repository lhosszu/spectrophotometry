package com.uni.spectro.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uni.spectro.R
import com.uni.spectro.ui.experiments.ExperimentsActivity
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ExperimentsUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ExperimentsActivity::class.java)

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
        onView(withId(R.id.toolbar_experiments_page)).check(matches(isDisplayed()))
    }

    @Test
    fun canDisplayBatteryLevel() {
        onView(withId(R.id.image_battery_level_experiments)).check(matches(isDisplayed()))
    }

    @Test
    fun canClickExperimentItem() {
    }

}