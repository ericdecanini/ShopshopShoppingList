package com.ericthecoder.shopshopshoppinglist.di.module.app.activity.settings

import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.shopshopshoppinglist.di.module.app.activity.ActivityModule
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.settings.SettingsActivity
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(includes = [ActivityModule::class])
class SettingsModule {

    @Provides
    fun provideActivity(activity: SettingsActivity): AppCompatActivity = activity
}
