package com.ericdecanini.shopshopshoppinglist.usecases.python

interface PythonDatabaseWrapper {

    fun getShoppingListsJson(): String

    fun getShoppingListJsonById(id: Int): String

    fun insertShoppingList(name: String): String

    fun insertShopItem(listId: Int, name: String, quantity: Int, checked: Boolean): String

    fun updateShoppingList(id: Int, name: String): String

    fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean): String

    fun deleteShoppingList(id: Int)

    fun deleteShopItem(id: Int)

    fun cleanup()
}
