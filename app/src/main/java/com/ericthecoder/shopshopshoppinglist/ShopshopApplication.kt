package com.ericthecoder.shopshopshoppinglist

import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.di.DaggerAppComponent
import com.ericthecoder.shopshopshoppinglist.library.firebase.admob.AppOpenAdManager
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import com.google.android.gms.ads.MobileAds
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

/**
 * Material 3 Migration:
 * Step 1 - Set theme colours and typography for base theme
 * Step 2 - Change widgets based on theme
 * Step 3 - Apply dynamic colour UI
 */
class ShopshopApplication : DaggerApplication() {

    @Inject
    lateinit var persistentStorageReader: PersistentStorageReader
    @Inject
    lateinit var persistentStorageWriter: PersistentStorageWriter
    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        AppOpenAdManager.initialize(this, persistentStorageReader)
    }
}
