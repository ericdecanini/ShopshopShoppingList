package com.ericdecanini.shopshopshoppinglist.usecases.repository

import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList

interface ShoppingListRepository {

    fun getShoppingLists(): List<ShoppingList>?

    fun getShoppingListById(id: Int): ShoppingList?

    fun createNewShoppingList(name: String): ShoppingList

    fun createNewShopItem(listId: Int, name: String): ShopItem

    fun updateShoppingList(id: Int, name: String): ShoppingList?

    fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean): ShopItem?

    fun deleteShoppingList(id: Int)

    fun deleteShopItem(id: Int)

}
