package com.ericthecoder.shopshopshoppinglist.di.module.app.activity

import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.ui.dialog.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.ui.dialog.DialogNavigatorImpl
import com.ericthecoder.shopshopshoppinglist.ui.snackbar.SnackbarNavigator
import com.ericthecoder.shopshopshoppinglist.ui.snackbar.SnackbarNavigatorImpl
import com.ericthecoder.shopshopshoppinglist.ui.toast.ToastNavigator
import com.ericthecoder.shopshopshoppinglist.ui.toast.ToastNavigatorImpl
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import com.ericthecoder.shopshopshoppinglist.util.navigator.NavigatorImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
class ActivityModule {

    @Provides
    fun provideActivityNavigator(originActivity: AppCompatActivity): Navigator =
        NavigatorImpl(originActivity)

    @Provides
    fun provideDialogNavigator(
        activity: AppCompatActivity,
        persistentStorageReader: PersistentStorageReader,
    ): DialogNavigator = DialogNavigatorImpl(activity, persistentStorageReader)

    @Provides
    fun provideToastNavigator(activity: AppCompatActivity): ToastNavigator =
        ToastNavigatorImpl(activity)

    @Provides
    fun provideSnackbarNavigator(
        activity: AppCompatActivity,
        resourceProvider: ResourceProvider,
    ): SnackbarNavigator = SnackbarNavigatorImpl(activity, resourceProvider)
}
