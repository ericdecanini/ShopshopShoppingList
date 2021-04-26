package com.ericdecanini.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test

class SharedPrefsWriterTest {

    private val sharedPreferences: SharedPreferences = mock()
    private val keys: SharedPrefsKeys = mock()
    private val persistentStorageWriter get() = SharedPrefsWriter(sharedPreferences, keys)

    private val sharedPreferencesEditor: SharedPreferences.Editor = mock()
    private val commonSharedPrefKey = "common_key"

    @Before
    fun setup() {
        given(sharedPreferences.edit()).willReturn(sharedPreferencesEditor)
    }

    @Test
    fun whenOnboardingShownToUser_thenStoredToPreferences() {
        given(keys.KEY_ONBOARDING_SHOWN).willReturn(commonSharedPrefKey)
        val hasShown = true

        persistentStorageWriter.setOnboardingShown(hasShown)

        inOrder(sharedPreferencesEditor) {
            verify(sharedPreferencesEditor).putBoolean(commonSharedPrefKey, hasShown)
            verify(sharedPreferencesEditor).apply()
        }
    }
}
