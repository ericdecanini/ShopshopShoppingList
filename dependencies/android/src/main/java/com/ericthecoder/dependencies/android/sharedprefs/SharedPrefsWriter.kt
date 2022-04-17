package com.ericthecoder.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter

class SharedPrefsWriter(
    sharedPreferences: SharedPreferences,
    private val keys: SharedPrefsKeys
) : PersistentStorageWriter {

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    override fun setOnboardingShown(onboardingShown: Boolean) {
        saveBoolean(keys.KEY_ONBOARDING_SHOWN, onboardingShown)
    }

    override fun setPremiumStatus(premiumStatus: PremiumStatus) {
        saveInt(keys.KEY_IS_PREMIUM, premiumStatus.code)
    }

    override fun setCurrentThemeColorIndex(index: Int) {
        saveInt(keys.KEY_CURRENT_THEME_COLOR_INDEX, index)
    }

    private fun saveBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun saveInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }
}
