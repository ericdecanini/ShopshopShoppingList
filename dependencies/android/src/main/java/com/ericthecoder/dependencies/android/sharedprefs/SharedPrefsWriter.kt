package com.ericthecoder.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter

class SharedPrefsWriter(
    sharedPreferences: SharedPreferences,
    private val keys: SharedPrefsKeys
) : PersistentStorageWriter {

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    override fun setOnboardingShown(onboardingShown: Boolean) {
        saveBoolean(keys.KEY_ONBOARDING_SHOWN, onboardingShown)
    }

    private fun saveBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }
}
