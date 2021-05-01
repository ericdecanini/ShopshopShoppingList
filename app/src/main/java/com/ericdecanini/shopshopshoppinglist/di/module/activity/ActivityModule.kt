package com.ericdecanini.shopshopshoppinglist.di.module.activity

import androidx.appcompat.app.AppCompatActivity
import com.ericdecanini.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericdecanini.shopshopshoppinglist.ui.dialogs.DialogNavigatorImpl
import com.ericdecanini.shopshopshoppinglist.ui.toast.ToastNavigator
import com.ericdecanini.shopshopshoppinglist.ui.toast.ToastNavigatorImpl
import com.ericdecanini.shopshopshoppinglist.util.navigator.Navigator
import com.ericdecanini.shopshopshoppinglist.util.navigator.NavigatorImpl
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
