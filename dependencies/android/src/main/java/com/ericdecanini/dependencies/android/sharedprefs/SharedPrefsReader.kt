package com.ericdecanini.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.ericdecanini.shopshopshoppinglist.usecases.storage.PersistentStorageReader

class SharedPrefsReader(
    private val sharedPreferences: SharedPreferences,
    private val keys: SharedPrefsKeys
) : PersistentStorageReader {

    override fun hasOnboardingShown() =
        sharedPreferences.getBoolean(keys.KEY_ONBOARDING_SHOWN, false)
}
