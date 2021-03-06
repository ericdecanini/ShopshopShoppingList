package com.ericthecoder.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader

class SharedPrefsReader(
    private val sharedPreferences: SharedPreferences,
    private val keys: SharedPrefsKeys
) : PersistentStorageReader {

    override fun hasOnboardingShown() =
        sharedPreferences.getBoolean(keys.KEY_ONBOARDING_SHOWN, false)

    override fun getPremiumStatus() =
        sharedPreferences.getInt(keys.KEY_IS_PREMIUM, 0).let { premiumCode ->
            PremiumStatus.values().first { it.code == premiumCode }
        }
}
