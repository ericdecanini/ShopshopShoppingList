package com.ericthecoder.shopshopshoppinglist.di.module.activity

import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigatorImpl
import com.ericthecoder.shopshopshoppinglist.ui.toast.ToastNavigator
import com.ericthecoder.shopshopshoppinglist.ui.toast.ToastNavigatorImpl
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import com.ericthecoder.shopshopshoppinglist.util.navigator.NavigatorImpl
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @Provides
    fun provideActivityNavigator(originActivity: AppCompatActivity): Navigator =
        NavigatorImpl(originActivity)

    @Provides
    fun provideDialogNavigator(activity: AppCompatActivity): DialogNavigator
            = DialogNavigatorImpl(activity)

    @Provides
    fun provideToastNavigator(activity: AppCompatActivity): ToastNavigator
            = ToastNavigatorImpl(activity)
}