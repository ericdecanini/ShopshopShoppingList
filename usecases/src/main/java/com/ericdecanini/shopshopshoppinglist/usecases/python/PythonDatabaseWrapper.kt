package com.ericdecanini.shopshopshoppinglist.usecases.python

interface PythonDatabaseWrapper {

    fun getShoppingListsJson(): String

    fun getShoppingListJsonById(id: Int): String

    fun cleanup()
}
