package com.ericthecoder.shopshopshoppinglist.di.module.dependencies.billing

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
class BillingModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun provideBillingInteractor(
        context: Context,
        activity: AppCompatActivity,
        resourceProvider: ResourceProvider,
    ): BillingInteractor = BillingInteractor(context, activity, resourceProvider)
}
