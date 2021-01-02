package com.ericdecanini.shopshopshoppinglist

import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.ericdecanini.shopshopshoppinglist.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class ShopshopApplication: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Python.start(AndroidPlatform(this))
    }
}
