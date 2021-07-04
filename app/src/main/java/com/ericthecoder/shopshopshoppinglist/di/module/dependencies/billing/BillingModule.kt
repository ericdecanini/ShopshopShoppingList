package com.ericthecoder.shopshopshoppinglist.di.module.dependencies.billing

import android.content.Context
import com.ericthecoder.dependencies.android.activity.TopActivityProvider
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
        topActivityProvider: TopActivityProvider,
        resourceProvider: ResourceProvider,
    ): BillingInteractor = BillingInteractor(context, topActivityProvider, resourceProvider)
}
