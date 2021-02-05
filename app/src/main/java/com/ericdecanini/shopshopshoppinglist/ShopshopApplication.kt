package com.ericdecanini.shopshopshoppinglist

import com.ericdecanini.shopshopshoppinglist.di.DaggerAppComponent
import com.ericdecanini.shopshopshoppinglist.usecases.database.PythonDatabaseWrapper
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class ShopshopApplication: DaggerApplication() {

    @Inject
    lateinit var pythonDatabaseWrapper: PythonDatabaseWrapper

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onTerminate() {
        pythonDatabaseWrapper.cleanup()
        super.onTerminate()
    }
}
