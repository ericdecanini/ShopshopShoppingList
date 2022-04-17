package com.ericthecoder.shopshopshoppinglist.usecases.service

import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

interface ShoppingListDatabaseService {

    suspend fun getShoppingLists(): List<ShoppingList>

    suspend fun getShoppingListById(id: Int): ShoppingList

    suspend fun createShoppingList(name: String): Int

    suspend fun updateShoppingList(shoppingList: ShoppingList)

    suspend fun deleteShoppingList(id: Int)
}
