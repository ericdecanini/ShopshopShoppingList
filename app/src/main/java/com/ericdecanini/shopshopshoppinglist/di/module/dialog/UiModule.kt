package com.ericdecanini.shopshopshoppinglist.di.module.dialog

import androidx.appcompat.app.AppCompatActivity
import com.ericdecanini.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericdecanini.shopshopshoppinglist.ui.dialogs.DialogNavigatorImpl
import com.ericdecanini.shopshopshoppinglist.ui.toast.ToastNavigator
import com.ericdecanini.shopshopshoppinglist.ui.toast.ToastNavigatorImpl
import dagger.Module
import dagger.Provides

@Module
class UiModule {

  @Provides
  fun provideDialogNavigator(activity: AppCompatActivity): DialogNavigator
      = DialogNavigatorImpl(activity)

  @Provides
  fun provideToastNavigator(activity: AppCompatActivity): ToastNavigator
          = ToastNavigatorImpl(activity)

}
