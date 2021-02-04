package com.ericdecanini.shopshopshoppinglist.dataaccess.repository

import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService

class SQLiteShoppingListRepository(
    private val shoppingListDatabaseService: ShoppingListDatabaseService
) : ShoppingListRepository {

    override fun getShoppingLists() = shoppingListDatabaseService.getShoppingLists()

    override fun getShoppingListById(id: Int) = shoppingListDatabaseService.getShoppingListById(id)
}
