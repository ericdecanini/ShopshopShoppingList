package com.ericdecanini.shopshopshoppinglist.di.module.dialog

import androidx.appcompat.app.AppCompatActivity
import com.ericdecanini.shopshopshoppinglist.dialogs.DialogNavigator
import com.ericdecanini.shopshopshoppinglist.dialogs.DialogNavigatorImpl
import dagger.Module
import dagger.Provides

@Module
class DialogModule {

  @Provides
  fun provideDialogNavigator(activity: AppCompatActivity): DialogNavigator
      = DialogNavigatorImpl(activity)

}
