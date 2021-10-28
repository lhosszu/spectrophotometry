package com.uni.spectro.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import java.util.*

class ContextUtil(base: Context) : ContextWrapper(base) {

    companion object {
        fun changeLocale(inputContext: Context, newLocale: Locale): ContextWrapper {
            var context: Context = inputContext
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration

            val localeList = LocaleList(newLocale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)

            context = context.createConfigurationContext(configuration)
            return ContextUtil(context)
        }
    }
}