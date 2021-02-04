package com.ericdecanini.shopshopshoppinglist.usecases.repository

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList

interface ShoppingListRepository {

    fun getShoppingLists(): List<ShoppingList>

    fun getShoppingListById(id: Int): ShoppingList

}
