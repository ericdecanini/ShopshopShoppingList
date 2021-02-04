package com.ericdecanini.shopshopshoppinglist.dataaccess.repository

import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService

class SQLiteShoppingListRepository(
    shoppingListDatabaseService: ShoppingListDatabaseService
) : ShoppingListRepository {

    override fun getShoppingLists() {
        TODO("Not yet implemented")
    }

    override fun getShoppingListById(id: Int) {
        TODO("Not yet implemented")
    }
}
