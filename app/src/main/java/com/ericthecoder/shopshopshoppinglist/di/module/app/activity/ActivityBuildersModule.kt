package com.ericthecoder.shopshopshoppinglist.di.module.app.activity

import com.ericthecoder.shopshopshoppinglist.di.module.app.activity.main.MainModule
import com.ericthecoder.shopshopshoppinglist.di.module.app.activity.onboarding.OnboardingModule
import com.ericthecoder.shopshopshoppinglist.di.module.app.activity.settings.SettingsModule
import com.ericthecoder.shopshopshoppinglist.di.module.app.activity.upsell.UpsellModule
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.onboarding.OnboardingActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.settings.SettingsActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [OnboardingModule::class])
    abstract fun contributeOnboardingActivity(): OnboardingActivity

    @ContributesAndroidInjector(modules = [UpsellModule::class])
    abstract fun contributeUpsellActivity(): UpsellActivity

    @ContributesAndroidInjector(modules = [SettingsModule::class])
    abstract fun contributeSettingsActivity(): SettingsActivity
}
