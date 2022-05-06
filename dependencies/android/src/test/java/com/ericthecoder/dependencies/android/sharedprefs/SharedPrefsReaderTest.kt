package com.ericthecoder.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.entities.theme.Theme
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
        every { keys.KEY_ONBOARDING_SHOWN } returns COMMON_KEY
        every { sharedPreferences.getBoolean(COMMON_KEY, false) } returns hasOnboardingShown

        val result = persistentStorageReader.hasOnboardingShown()

        assertThat(result).isEqualTo(hasOnboardingShown)
    }

    @Test
    fun `getPremiumStatus returns from preferences`() {
        val premiumStatus = PremiumStatus.PREMIUM
        every { keys.KEY_PREMIUM_STATUS } returns COMMON_KEY
        every { sharedPreferences.getInt(COMMON_KEY, 0) } returns premiumStatus.code

        val result = persistentStorageReader.getPremiumStatus()

        assertThat(result).isEqualTo(premiumStatus)
    }

    @Test
    fun `getCurrentTheme returns from preferences`() {
        val currentTheme = Theme.BLUE
        every { keys.KEY_CURRENT_THEME } returns COMMON_KEY
        every { sharedPreferences.getString(COMMON_KEY, Theme.BLUE.name) } returns currentTheme.name

        val result = persistentStorageReader.getCurrentTheme()

        assertThat(result).isEqualTo(currentTheme.name)
    }

    companion object {
        private const val COMMON_KEY = "KEY"
    }
}
