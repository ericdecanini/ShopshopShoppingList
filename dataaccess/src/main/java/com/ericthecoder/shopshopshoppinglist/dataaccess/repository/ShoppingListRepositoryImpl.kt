package com.ericthecoder.shopshopshoppinglist.dataaccess.repository

import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService

class ShoppingListRepositoryImpl(
    private val databaseService: ShoppingListDatabaseService,
) : ShoppingListRepository {

    override suspend fun getShoppingLists() = databaseService.getShoppingLists()
        .onEach(::sortItems)

    private fun sortItems(shoppingList: ShoppingList) {
        shoppingList.items.sortBy { it.checked }
    }

    override suspend fun getShoppingListById(id: Int) = databaseService.getShoppingListById(id)
        .also(::sortItems)

    override suspend fun createNewShoppingList(name: String): ShoppingList {
        val id = databaseService.createShoppingList(name)
        return databaseService.getShoppingListById(id)
    }

    override suspend fun updateShoppingList(shoppingList: ShoppingList) =
        databaseService.updateShoppingList(shoppingList)

    override suspend fun deleteShoppingList(id: Int) =
        databaseService.deleteShoppingList(id)
}
