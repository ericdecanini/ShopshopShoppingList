package com.ericthecoder.shopshopshoppinglist.di.module.app.activity.upsell

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.ericthecoder.shopshopshoppinglist.di.module.app.ViewModelKey
import com.ericthecoder.shopshopshoppinglist.di.module.app.activity.ActivityModule
import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.billing.BillingModule
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(includes = [
    ActivityModule::class,
    UpsellModule.ViewModelModule::class,
    BillingModule::class,
])
class UpsellModule {

    @Provides
    fun provideActivity(activity: UpsellActivity): AppCompatActivity = activity

    @Module
    internal abstract class ViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(UpsellViewModel::class)
        internal abstract fun bindUpsellViewModule(viewModel: UpsellViewModel): ViewModel
    }

}
