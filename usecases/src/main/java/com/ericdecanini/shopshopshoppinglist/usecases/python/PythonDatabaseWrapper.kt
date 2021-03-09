package com.ericdecanini.shopshopshoppinglist.usecases.python

interface PythonDatabaseWrapper {

    suspend fun getShoppingListsJson(): String

    suspend fun getShoppingListJsonById(id: Int): String

    suspend fun insertShoppingList(name: String): String

    suspend fun insertShopItem(listId: Int, name: String, quantity: Int, checked: Boolean): String

    suspend fun updateShoppingList(id: Int, name: String): String

    suspend fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean): String

    suspend fun deleteShoppingList(id: Int)

    suspend fun deleteShopItem(id: Int)

    suspend fun cleanup()
}
