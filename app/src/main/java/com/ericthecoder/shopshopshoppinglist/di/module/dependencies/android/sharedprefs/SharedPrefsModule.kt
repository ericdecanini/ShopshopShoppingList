package com.ericthecoder.shopshopshoppinglist.di.module.dependencies.android.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.ericthecoder.dependencies.android.sharedprefs.SharedPrefsKeys
import com.ericthecoder.dependencies.android.sharedprefs.SharedPrefsReader
import com.ericthecoder.dependencies.android.sharedprefs.SharedPrefsWriter
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import dagger.Module
import dagger.Provides

@Module
class SharedPrefsModule {

    @Provides
    fun provideSharedPrefsKeys(): SharedPrefsKeys = SharedPrefsKeys()

    @Provides
    fun provideSharedPreferences(
        context: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun providePersistentStorageReader(
        sharedPreferences: SharedPreferences,
        keys: SharedPrefsKeys
    ): PersistentStorageReader = SharedPrefsReader(sharedPreferences, keys)

    @Provides
    fun providePersistentStorageWriter(
        sharedPreferences: SharedPreferences,
        keys: SharedPrefsKeys
    ): PersistentStorageWriter = SharedPrefsWriter(sharedPreferences, keys)

}
