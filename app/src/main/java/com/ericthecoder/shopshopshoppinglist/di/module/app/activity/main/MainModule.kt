package com.ericthecoder.shopshopshoppinglist.di.module.app.activity.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.ericthecoder.shopshopshoppinglist.di.module.app.ViewModelKey
import com.ericthecoder.shopshopshoppinglist.di.module.app.activity.ActivityModule
import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.billing.BillingModule
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(includes = [
    MainFragmentsModule::class,
    ActivityModule::class,
    MainModule.ViewModelModule::class,
    BillingModule::class,
])
class MainModule {

    @Provides
    fun provideActivity(activity: MainActivity): AppCompatActivity = activity

    @Module
    internal abstract class ViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(MainViewModel::class)
        internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
    }
}
