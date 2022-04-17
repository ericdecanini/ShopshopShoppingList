package com.ericthecoder.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.nhaarman.mockitokotlin2.given
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SharedPrefsWriterTest {

    private val sharedPreferences: SharedPreferences = mockk()
    private val keys: SharedPrefsKeys = mockk()
    private val persistentStorageWriter get() = SharedPrefsWriter(sharedPreferences, keys)

    private val sharedPreferencesEditor: SharedPreferences.Editor = mockk()

    @BeforeEach
    fun setup() {
        given(sharedPreferences.edit()).willReturn(sharedPreferencesEditor)
    }

    @Test
    fun `setOnboardingShown saves to preferences`() {
        every { keys.KEY_ONBOARDING_SHOWN } returns COMMON_PREF_KEY
        val hasShown = true

        persistentStorageWriter.setOnboardingShown(hasShown)


        verifyOrder {
            sharedPreferencesEditor.putBoolean(COMMON_PREF_KEY, hasShown)
            sharedPreferencesEditor.apply()
        }
    }

    @Test
    fun `getPremiumStatus saves to preferences`() {
        every { keys.KEY_PREMIUM_STATUS } returns COMMON_PREF_KEY
        val premiumStatus = PremiumStatus.PREMIUM

        persistentStorageWriter.setPremiumStatus(premiumStatus)


        verifyOrder {
            sharedPreferencesEditor.putInt(COMMON_PREF_KEY, premiumStatus.code)
            sharedPreferencesEditor.apply()
        }
    }

    @Test
    fun `getCurrentTheme saves to preferences`() {
        every { keys.KEY_CURRENT_THEME } returns COMMON_PREF_KEY
        val currentTheme = 1

        persistentStorageWriter.setCurrentTheme(currentTheme)


        verifyOrder {
            sharedPreferencesEditor.putInt(COMMON_PREF_KEY, currentTheme)
            sharedPreferencesEditor.apply()
        }
    }

    @Test
    fun `setHasChangedTheme saves to preferences`() {
        every { keys.KEY_HAS_CHANGED_THEME } returns COMMON_PREF_KEY
        val hasChangedTheme = true

        persistentStorageWriter.setHasChangedTheme(hasChangedTheme)


        verifyOrder {
            sharedPreferencesEditor.putBoolean(COMMON_PREF_KEY, hasChangedTheme)
            sharedPreferencesEditor.apply()
        }
    }

    companion object {
        private const val COMMON_PREF_KEY = "common_key"
    }
}
