package com.ericthecoder.shopshopshoppinglist.di.module.dependencies.billing

import com.ericdecanini.shopshopshoppinglist.billing.BillingClientInitializerImpl
import com.ericthecoder.shopshopshoppinglist.usecases.initializer.BillingClientInitializer
import dagger.Module
import dagger.Provides

@Module
class BillingModule {

    @Provides
    fun provideBillingClientInitializer(): BillingClientInitializer = BillingClientInitializerImpl()
}
