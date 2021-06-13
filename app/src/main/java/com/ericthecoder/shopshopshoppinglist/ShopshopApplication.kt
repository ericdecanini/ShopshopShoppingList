package com.ericthecoder.shopshopshoppinglist

import com.ericthecoder.dependencies.android.activity.TopActivityProvider
import com.ericthecoder.shopshopshoppinglist.di.DaggerAppComponent
import com.ericthecoder.shopshopshoppinglist.library.firebase.admob.AppOpenAdManager
import com.ericthecoder.shopshopshoppinglist.usecases.initializer.AppInitializer
import com.google.android.gms.ads.MobileAds
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class ShopshopApplication: DaggerApplication() {

    @Inject lateinit var appInitializer: AppInitializer
    @Inject lateinit var topActivityProvider: TopActivityProvider

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        AppOpenAdManager.initialize(this)
        appInitializer.initialize()
        topActivityProvider.setupWith(this)
    }
}
