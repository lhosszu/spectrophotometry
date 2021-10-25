package com.uni.spectro.preferences

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.uni.spectro.preferences.GlobalSettings.AUTO_CONNECT
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferenceManagerIntegrationTest {

    private val preferenceManager: PreferenceManager = PreferenceManager.instance

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        PreferenceManager.instance.initialize(appContext)
    }

    @Test
    fun canSetPreference() {
        // given
        val beforeSwitching: Boolean = preferenceManager.getPreference(AUTO_CONNECT)

        // when
        preferenceManager.setPreference(AUTO_CONNECT, !beforeSwitching)
        val afterSwitching: Boolean = preferenceManager.getPreference(AUTO_CONNECT)

        // then
        assertThat(beforeSwitching).isNotEqualTo(afterSwitching)
    }
}