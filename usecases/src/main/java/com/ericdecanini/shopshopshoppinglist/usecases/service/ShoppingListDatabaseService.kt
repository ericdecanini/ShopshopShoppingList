package com.ericdecanini.shopshopshoppinglist.usecases.service

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList

interface ShoppingListDatabaseService {

    fun getShoppingLists(): List<ShoppingList>

    fun getShoppingListById(id: Int): ShoppingList

}
