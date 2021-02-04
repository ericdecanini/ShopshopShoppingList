package com.ericdecanini.shopshopshoppinglist.usecases.database

interface PythonDatabaseWrapper {

    fun getShoppingListsJson(): String

    fun getShoppingListJsonById(id: Int): String
}
