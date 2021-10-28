package com.uni.spectro.root

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import com.uni.spectro.preferences.GlobalSettings
import com.uni.spectro.preferences.PreferenceManager
import com.uni.spectro.util.ContextUtil
import java.util.*

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(context: Context) {
        val language: String = if (PreferenceManager.instance.getPreference(GlobalSettings.LANGUAGE)) "hu" else "en"
        val localeUpdatedContext: ContextWrapper = ContextUtil.changeLocale(context, Locale(language))
        super.attachBaseContext(localeUpdatedContext)
    }
}