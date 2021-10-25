package com.uni.spectro.persistence.util

import io.realm.RealmList

class RealmDataConverter {
    companion object {
        fun <T> toArrayList(realmList: RealmList<T>): ArrayList<T> {
            val result: ArrayList<T> = ArrayList(realmList.size)
            result.addAll(realmList)
            return result
        }
    }
}