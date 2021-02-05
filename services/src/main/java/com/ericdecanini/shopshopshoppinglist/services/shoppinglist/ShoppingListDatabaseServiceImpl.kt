package com.ericdecanini.shopshopshoppinglist.services.shoppinglist

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.usecases.database.PythonDatabaseWrapper
import com.ericdecanini.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService

class ShoppingListDatabaseServiceImpl(
    private val pythonDatabaseWrapper: PythonDatabaseWrapper
) : ShoppingListDatabaseService {

    override fun getShoppingLists(): List<ShoppingList> {
        TODO("Not yet implemented")
    }

    override fun getShoppingListById(id: Int): ShoppingList {
        TODO("Not yet implemented")
    }
}
