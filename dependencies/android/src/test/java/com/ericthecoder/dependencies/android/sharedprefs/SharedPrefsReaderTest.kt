package com.ericthecoder.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SharedPrefsReaderTest {

    private val sharedPreferences: SharedPreferences = mock()
    private val keys: SharedPrefsKeys = mock()
    private val persistentStorageReader = SharedPrefsReader(sharedPreferences, keys)

    private val commonSharedPrefKey = "common_key"

    @Test
    fun whenGettingOnboardingShown_thenReturnFromPreferences() {
        val hasOnboardingShown = true
        given(keys.KEY_ONBOARDING_SHOWN).willReturn(commonSharedPrefKey)
        given(sharedPreferences.getBoolean(commonSharedPrefKey, false)).willReturn(hasOnboardingShown)

        val result = persistentStorageReader.hasOnboardingShown()

        assertThat(result).isEqualTo(hasOnboardingShown)
    }
}
