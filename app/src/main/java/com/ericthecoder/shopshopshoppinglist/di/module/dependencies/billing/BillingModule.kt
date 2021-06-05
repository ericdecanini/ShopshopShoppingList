package com.ericthecoder.shopshopshoppinglist.di.module.dependencies.billing

import android.content.Context
import com.ericdecanini.shopshopshoppinglist.billing.BillingRepositoryImpl
import com.ericthecoder.shopshopshoppinglist.usecases.repository.BillingRepository
import dagger.Module
import dagger.Provides

@Module
class BillingModule {

    @Provides
    fun provideBillingRepository(
        context: Context
    ): BillingRepository = BillingRepositoryImpl(context)
}
