package com.ericthecoder.shopshopshoppinglist.mvvm.activity.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ericthecoder.shopshopshoppinglist.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}