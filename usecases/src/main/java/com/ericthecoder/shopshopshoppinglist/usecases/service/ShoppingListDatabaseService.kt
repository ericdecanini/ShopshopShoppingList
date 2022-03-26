package com.ericthecoder.shopshopshoppinglist.usecases.service

import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

interface ShoppingListDatabaseService {

    suspend fun getShoppingLists(): List<ShoppingList>

    suspend fun getShoppingListById(id: Int): ShoppingList

    suspend fun createShoppingList(name: String): ShoppingList

    suspend fun createShopItem(listId: Int, name: String): ShopItem

    suspend fun updateShoppingList(id: Int, name: String): ShoppingList

    suspend fun updateShopItem(currentName: String, newName: String, quantity: Int, checked: Boolean): ShopItem

    suspend fun deleteShoppingList(id: Int)

    suspend fun deleteShopItem(name: String)

}
