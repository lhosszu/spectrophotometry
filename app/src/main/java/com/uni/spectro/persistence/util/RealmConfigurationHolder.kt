package com.uni.spectro.persistence.util

import com.uni.spectro.persistence.RealmAttributes.REALM_NAME
import io.realm.RealmConfiguration

class RealmConfigurationHolder {
    companion object {
        fun config(): RealmConfiguration {
            return RealmConfiguration.Builder()
                    .name(REALM_NAME)
                    .deleteRealmIfMigrationNeeded()
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(false)
                    .build()
        }
    }
}