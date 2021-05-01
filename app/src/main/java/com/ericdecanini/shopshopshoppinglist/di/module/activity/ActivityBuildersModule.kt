package com.ericdecanini.shopshopshoppinglist.di.module.activity

import com.ericdecanini.shopshopshoppinglist.di.module.activity.main.MainModule
import com.ericdecanini.shopshopshoppinglist.di.module.activity.onboarding.OnboardingModule
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainActivity
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.onboarding.OnboardingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [OnboardingModule::class])
    abstract fun contributeOnboardingActivity(): OnboardingActivity
}
