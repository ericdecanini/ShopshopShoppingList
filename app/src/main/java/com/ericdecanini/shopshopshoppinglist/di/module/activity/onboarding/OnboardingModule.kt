package com.ericdecanini.shopshopshoppinglist.di.module.activity.onboarding

import androidx.appcompat.app.AppCompatActivity
import com.ericdecanini.shopshopshoppinglist.di.module.activity.ActivityModule
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.onboarding.OnboardingActivity
import dagger.Module
import dagger.Provides

@Module(includes = [ActivityModule::class])
class OnboardingModule {

    @Provides
    fun provideActivity(activity: OnboardingActivity): AppCompatActivity = activity
}
