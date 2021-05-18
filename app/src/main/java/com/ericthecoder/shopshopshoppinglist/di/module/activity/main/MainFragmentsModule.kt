package com.ericthecoder.shopshopshoppinglist.di.module.activity.main

import com.ericthecoder.shopshopshoppinglist.di.module.fragment.HomeModule
import com.ericthecoder.shopshopshoppinglist.di.module.fragment.ListModule
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeFragment
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsModule {

    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [ListModule::class])
    abstract fun contributeListFragment(): ListFragment
}
