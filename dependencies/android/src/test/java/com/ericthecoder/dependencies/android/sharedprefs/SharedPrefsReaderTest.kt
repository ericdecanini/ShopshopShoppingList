package com.ericthecoder.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SharedPrefsReaderTest {

    private val sharedPreferences: SharedPreferences = mockk()
    private val keys: SharedPrefsKeys = mockk()
    private val persistentStorageReader = SharedPrefsReader(sharedPreferences, keys)

    @Test
    fun `hasOnboardingShown returns from preferences`() {
        val hasOnboardingShown = true
        every { keys.KEY_ONBOARDING_SHOWN } returns "key"
        every { sharedPreferences.getBoolean(keys.KEY_ONBOARDING_SHOWN, false) } returns hasOnboardingShown

        val result = persistentStorageReader.hasOnboardingShown()

        assertThat(result).isEqualTo(hasOnboardingShown)
    }

    @Test
    fun `getPremiumStatus returns from preferences`() {
        val premiumStatus = PremiumStatus.PREMIUM
        every { keys.KEY_PREMIUM_STATUS } returns "key"
        every { sharedPreferences.getInt(keys.KEY_PREMIUM_STATUS, 0) } returns premiumStatus.code

        val result = persistentStorageReader.getPremiumStatus()

        assertThat(result).isEqualTo(premiumStatus)
    }

    @Test
    fun `getCurrentTheme returns from preferences`() {
        val currentThemeCode = 1
        every { keys.KEY_CURRENT_THEME } returns "key"
        every { sharedPreferences.getInt(keys.KEY_CURRENT_THEME, 0) } returns currentThemeCode

        val result = persistentStorageReader.getCurrentTheme()

        assertThat(result).isEqualTo(currentThemeCode)
    }

    @Test
    fun `hasChangedTheme returns from preferences`() {
        val hasChangedTheme = true
        every { keys.KEY_HAS_CHANGED_THEME } returns "key"
        every { sharedPreferences.getBoolean(keys.KEY_HAS_CHANGED_THEME, false) } returns hasChangedTheme

        val result = persistentStorageReader.hasOnboardingShown()

        assertThat(result).isEqualTo(hasChangedTheme)
    }
}
