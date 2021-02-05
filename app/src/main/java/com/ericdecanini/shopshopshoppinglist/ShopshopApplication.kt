package com.ericdecanini.shopshopshoppinglist

import com.ericdecanini.shopshopshoppinglist.di.DaggerAppComponent
import com.ericdecanini.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper
import com.ericdecanini.shopshopshoppinglist.usecases.python.PythonInitializer
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class ShopshopApplication: DaggerApplication() {

    @Inject lateinit var pythonInitializer: PythonInitializer
    @Inject lateinit var pythonDatabaseWrapper: PythonDatabaseWrapper

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        pythonInitializer.initialize()
    }

    override fun onTerminate() {
        pythonDatabaseWrapper.cleanup()
        super.onTerminate()
    }
}
