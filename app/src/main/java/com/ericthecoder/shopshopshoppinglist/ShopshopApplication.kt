package com.ericthecoder.shopshopshoppinglist

import androidx.room.Room
import com.ericthecoder.dependencies.android.room.ShopshopDatabase
import com.ericthecoder.shopshopshoppinglist.di.DaggerAppComponent
import com.ericthecoder.shopshopshoppinglist.library.firebase.admob.AppOpenAdManager
import com.ericthecoder.shopshopshoppinglist.usecases.initializer.AppInitializer
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.google.android.gms.ads.MobileAds
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class ShopshopApplication: DaggerApplication() {

    @Inject lateinit var appInitializer: AppInitializer
    @Inject lateinit var persistentStorageReader: PersistentStorageReader

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        initializeDatabase()
        MobileAds.initialize(this)
        AppOpenAdManager.initialize(this, persistentStorageReader)
        appInitializer.initialize()
    }

    private fun initializeDatabase() {
        database = Room.databaseBuilder(
            this,
            ShopshopDatabase::class.java,
            "shopshop-db"
        ).build()
    }

    companion object {

         var database: ShopshopDatabase? = null
    }
}
