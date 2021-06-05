package com.ericthecoder.shopshopshoppinglist.usecases.initializer

class AppInitializer(
    private val pythonInitializer: PythonInitializer
) {

    fun initialize() {
        pythonInitializer.initialize()
    }
}
