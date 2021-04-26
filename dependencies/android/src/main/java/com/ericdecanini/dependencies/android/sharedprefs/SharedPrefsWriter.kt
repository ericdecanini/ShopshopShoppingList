package com.ericdecanini.dependencies.android.sharedprefs

import android.content.SharedPreferences
import com.ericdecanini.shopshopshoppinglist.usecases.storage.PersistentStorageWriter

class SharedPrefsWriter(
    sharedPreferences: SharedPreferences,
    private val keys: SharedPrefsKeys
) : PersistentStorageWriter {

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

}
