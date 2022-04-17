package com.ericthecoder.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SharedPrefsWriterTest {

    private val sharedPreferences: SharedPreferences = mockk()
    private val keys: SharedPrefsKeys = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private val persistentStorageWriter get() = SharedPrefsWriter(sharedPreferences, keys)

    @BeforeEach
    fun setup() {
        every { sharedPreferences.edit() } returns editor
        every { editor.putInt(any(), any()) } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        justRun { editor.apply() }
    }

    @Test
    fun `setOnboardingShown saves to preferences`() {
        every { keys.KEY_ONBOARDING_SHOWN } returns COMMON_PREF_KEY
        val hasShown = true

        persistentStorageWriter.setOnboardingShown(hasShown)


        verifyOrder {
            editor.putBoolean(COMMON_PREF_KEY, hasShown)
            editor.apply()
        }
    }

    @Test
    fun `getPremiumStatus saves to preferences`() {
        every { keys.KEY_PREMIUM_STATUS } returns COMMON_PREF_KEY
        val premiumStatus = PremiumStatus.PREMIUM

        persistentStorageWriter.setPremiumStatus(premiumStatus)


        verifyOrder {
            editor.putInt(COMMON_PREF_KEY, premiumStatus.code)
            editor.apply()
        }
    }

    @Test
    fun `getCurrentTheme saves to preferences`() {
        every { keys.KEY_CURRENT_THEME } returns COMMON_PREF_KEY
        val currentTheme = 1

        persistentStorageWriter.setCurrentTheme(currentTheme)


        verifyOrder {
            editor.putInt(COMMON_PREF_KEY, currentTheme)
            editor.apply()
        }
    }

    @Test
    fun `setHasChangedTheme saves to preferences`() {
        every { keys.KEY_HAS_CHANGED_THEME } returns COMMON_PREF_KEY
        val hasChangedTheme = true

        persistentStorageWriter.setHasChangedTheme(hasChangedTheme)


        verifyOrder {
            editor.putBoolean(COMMON_PREF_KEY, hasChangedTheme)
            editor.apply()
        }
    }

    companion object {
        private const val COMMON_PREF_KEY = "common_key"
    }
}
