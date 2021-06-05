package com.ericthecoder.shopshopshoppinglist.usecases.initializer

import com.ericthecoder.shopshopshoppinglist.usecases.python.PythonInitializer

class AppInitializer(
    private val billingClientInitializer: BillingClientInitializer,
    private val pythonInitializer: PythonInitializer
) {

    fun initialize() {
        billingClientInitializer.initialize()
        pythonInitializer.initialize()
    }
}
