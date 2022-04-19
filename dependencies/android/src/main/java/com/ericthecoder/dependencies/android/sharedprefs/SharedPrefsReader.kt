package com.ericthecoder.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader

class SharedPrefsReader(
    private val sharedPreferences: SharedPreferences,
    private val keys: SharedPrefsKeys,
) : PersistentStorageReader {

    override fun hasOnboardingShown() =
        sharedPreferences.getBoolean(keys.KEY_ONBOARDING_SHOWN, false)

    override fun getPremiumStatus() =
        sharedPreferences.getInt(keys.KEY_PREMIUM_STATUS, 0).let { premiumCode ->
            PremiumStatus.values().first { it.code == premiumCode }
        }

    override fun getCurrentTheme() =
        sharedPreferences.getInt(keys.KEY_CURRENT_THEME, 0)

    override fun hasChangedTheme() =
        sharedPreferences.getBoolean(keys.KEY_HAS_CHANGED_THEME, false)
}
