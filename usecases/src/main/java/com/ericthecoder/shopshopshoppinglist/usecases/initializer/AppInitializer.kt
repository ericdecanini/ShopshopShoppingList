package com.ericthecoder.shopshopshoppinglist.usecases.initializer

class AppInitializer(
    private val billingClientInitializer: BillingClientInitializer
) {

    fun initialize() {
        billingClientInitializer.initialize()
    }
}
