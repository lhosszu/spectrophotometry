package com.uni.spectro.persistence.util

import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class
RealmDataConverterTest {

    @Test
    fun whenConvertRealmListOfIntegers_thenReturnSameListContent() {
        // given
        val realmList = RealmList(1, 2, 3)

        // when
        val result = RealmDataConverter.Companion.toArrayList(realmList)

        // then
        assertThat(result).containsExactly(1, 2, 3)
    }
}