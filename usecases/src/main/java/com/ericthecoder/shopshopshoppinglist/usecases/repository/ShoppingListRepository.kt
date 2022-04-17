package com.ericthecoder.shopshopshoppinglist.usecases.repository

import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

interface ShoppingListRepository {

    suspend fun getShoppingLists(): List<ShoppingList>?

    suspend fun getShoppingListById(id: Int): ShoppingList

    suspend fun createNewShoppingList(name: String): ShoppingList

    suspend fun updateShoppingList(shoppingList: ShoppingList)

    suspend fun deleteShoppingList(id: Int)
}
