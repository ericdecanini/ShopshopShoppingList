package com.ericthecoder.shopshopshoppinglist.dataaccess.repository

import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService

class ShoppingListRepositoryImpl(
    private val shoppingListDatabaseService: ShoppingListDatabaseService,
) : ShoppingListRepository {

    override suspend fun getShoppingLists() =
        shoppingListDatabaseService.getShoppingLists()

    override suspend fun getShoppingListById(id: Int) =
        shoppingListDatabaseService.getShoppingListById(id)

    override suspend fun createNewShoppingList(name: String) =
        shoppingListDatabaseService.createShoppingList(name)

    override suspend fun createNewShopItem(listId: Int, name: String) =
        shoppingListDatabaseService.createShopItem(listId, name)

    override suspend fun updateShoppingList(id: Int, name: String) =
        shoppingListDatabaseService.updateShoppingList(id, name)

    override suspend fun updateShopItem(currentName: String, newName: String, quantity: Int, checked: Boolean) =
        shoppingListDatabaseService.updateShopItem(currentName, newName, quantity, checked)

    override suspend fun deleteShoppingList(id: Int) =
        shoppingListDatabaseService.deleteShoppingList(id)

    override suspend fun deleteShopItem(name: String) =
        shoppingListDatabaseService.deleteShopItem(name)
}
