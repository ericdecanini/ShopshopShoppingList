package com.ericthecoder.shopshopshoppinglist

import com.ericthecoder.shopshopshoppinglist.di.DaggerAppComponent
import com.ericthecoder.shopshopshoppinglist.library.firebase.admob.AppOpenAdManager
import com.ericthecoder.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper
import com.ericthecoder.shopshopshoppinglist.usecases.python.PythonInitializer
import com.ericthecoder.shopshopshoppinglist.util.providers.TopActivityProvider
import com.google.android.gms.ads.MobileAds
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class ShopshopApplication: DaggerApplication() {

    @Inject lateinit var pythonInitializer: PythonInitializer
    @Inject lateinit var pythonDatabaseWrapper: PythonDatabaseWrapper
    @Inject lateinit var topActivityProvider: TopActivityProvider

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        AppOpenAdManager.initialize(this)
        pythonInitializer.initialize()
        topActivityProvider.setupWith(this)
    }
}
