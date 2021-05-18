package com.ericthecoder.shopshopshoppinglist.usecases.service

import com.ericthecoder.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericthecoder.shopshopshoppinglist.entities.network.ShoppingListResponse

interface ShoppingListDatabaseService {

    suspend fun getShoppingLists(): List<ShoppingListResponse>?

    suspend fun getShoppingListById(id: Int): ShoppingListResponse?

    suspend fun createShoppingList(name: String): ShoppingListResponse

    suspend fun createShopItem(listId: Int, name: String): ShopItemResponse

    suspend fun updateShoppingList(id: Int, name: String): ShoppingListResponse?

    suspend fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean): ShopItemResponse?

    suspend fun deleteShoppingList(id: Int)

    suspend fun deleteShopItem(id: Int)

}
