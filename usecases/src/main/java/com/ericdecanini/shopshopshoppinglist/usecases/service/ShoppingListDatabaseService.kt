package com.ericdecanini.shopshopshoppinglist.usecases.service

import com.ericdecanini.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericdecanini.shopshopshoppinglist.entities.network.ShoppingListResponse

interface ShoppingListDatabaseService {

    fun getShoppingLists(): List<ShoppingListResponse>?

    fun getShoppingListById(id: Int): ShoppingListResponse?

    fun createShoppingList(name: String): ShoppingListResponse

    fun createShopItem(listId: Int, name: String): ShopItemResponse

    fun updateShoppingList(id: Int, name: String): ShoppingListResponse?

    fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean): ShopItemResponse?

    fun deleteShoppingList(id: Int)

    fun deleteShopItem(id: Int)

}
