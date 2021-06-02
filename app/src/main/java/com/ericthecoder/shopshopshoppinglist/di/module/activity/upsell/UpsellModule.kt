package com.ericthecoder.shopshopshoppinglist.di.module.activity.upsell

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.ericthecoder.shopshopshoppinglist.di.ViewModelKey
import com.ericthecoder.shopshopshoppinglist.di.module.activity.ActivityModule
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [
    ActivityModule::class,
    UpsellModule.ViewModelModule::class
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
