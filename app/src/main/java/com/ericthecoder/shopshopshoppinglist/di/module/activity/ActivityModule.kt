package com.ericthecoder.shopshopshoppinglist.di.module.activity

import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigatorImpl
import com.ericthecoder.shopshopshoppinglist.ui.toast.ToastNavigator
import com.ericthecoder.shopshopshoppinglist.ui.toast.ToastNavigatorImpl
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import com.ericthecoder.shopshopshoppinglist.util.navigator.NavigatorImpl
import com.ericthecoder.shopshopshoppinglist.util.providers.TopActivityProvider
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @Provides
    fun provideActivityNavigator(topActivityProvider: TopActivityProvider): Navigator =
        NavigatorImpl(topActivityProvider)

    @Provides
    fun provideDialogNavigator(topActivityProvider: TopActivityProvider): DialogNavigator =
        DialogNavigatorImpl(topActivityProvider)

    @Provides
    fun provideToastNavigator(topActivityProvider: TopActivityProvider): ToastNavigator =
        ToastNavigatorImpl(topActivityProvider)
}
