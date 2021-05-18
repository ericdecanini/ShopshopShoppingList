package com.ericthecoder.shopshopshoppinglist.di.module.activity

import com.ericthecoder.shopshopshoppinglist.di.module.activity.main.MainModule
import com.ericthecoder.shopshopshoppinglist.di.module.activity.onboarding.OnboardingModule
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.onboarding.OnboardingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [OnboardingModule::class])
    abstract fun contributeOnboardingActivity(): OnboardingActivity
}
