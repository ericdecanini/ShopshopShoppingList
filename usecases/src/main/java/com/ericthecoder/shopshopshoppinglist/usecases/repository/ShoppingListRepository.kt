package com.ericthecoder.shopshopshoppinglist.usecases.repository

import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

interface ShoppingListRepository {

    suspend fun getShoppingLists(): List<ShoppingList>?

    suspend fun getShoppingListById(id: Int): ShoppingList

    suspend fun createNewShoppingList(name: String): ShoppingList

    suspend fun createNewShopItem(listId: Int, name: String): ShopItem

    suspend fun updateShoppingList(id: Int, name: String): ShoppingList

    suspend fun updateShopItem(currentName: String, newName: String, quantity: Int, checked: Boolean): ShopItem?

    suspend fun deleteShoppingList(id: Int)

    suspend fun deleteShopItem(name: String)

}
