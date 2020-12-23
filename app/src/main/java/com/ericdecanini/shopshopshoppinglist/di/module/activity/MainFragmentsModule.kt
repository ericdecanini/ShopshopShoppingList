package com.ericdecanini.shopshopshoppinglist.di.module.activity

import com.ericdecanini.shopshopshoppinglist.di.module.fragment.HomeModule
import com.ericdecanini.shopshopshoppinglist.di.module.fragment.ListModule
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home.HomeFragment
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsModule {

    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [ListModule::class])
    abstract fun contributeListFragment(): ListFragment
}
