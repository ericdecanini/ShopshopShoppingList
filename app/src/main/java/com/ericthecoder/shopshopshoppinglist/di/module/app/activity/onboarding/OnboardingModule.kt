package com.ericthecoder.shopshopshoppinglist.di.module.app.activity.onboarding

import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.shopshopshoppinglist.di.module.app.activity.ActivityModule
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.onboarding.OnboardingActivity
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(includes = [ActivityModule::class])
class OnboardingModule {

    @Provides
    fun provideActivity(activity: OnboardingActivity): AppCompatActivity = activity
}
