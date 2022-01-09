package com.ericthecoder.shopshopshoppinglist.dataaccess.repository

import com.ericthecoder.shopshopshoppinglist.dataaccess.mapper.ShoppingListMapper
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService

class SQLiteShoppingListRepository(
    private val shoppingListDatabaseService: ShoppingListDatabaseService,
    private val shoppingListMapper: ShoppingListMapper,
) : ShoppingListRepository {

    override suspend fun getShoppingLists() = shoppingListDatabaseService.getShoppingLists()
        .map { shoppingListMapper.mapResponseToShoppingList(it) }

    override suspend fun getShoppingListById(id: Int) = shoppingListDatabaseService.getShoppingListById(id)
        .let { shoppingListMapper.mapResponseToShoppingList(it) }

    override suspend fun createNewShoppingList(name: String) = shoppingListDatabaseService.createShoppingList(name)
        .let { shoppingListMapper.mapResponseToShoppingList(it) }

    override suspend fun createNewShopItem(listId: Int, name: String) = shoppingListDatabaseService.createShopItem(listId, name)
        .let { shoppingListMapper.mapResponseToShopItem(it) }

    override suspend fun updateShoppingList(id: Int, name: String) = shoppingListDatabaseService.updateShoppingList(id, name)
        .let { shoppingListMapper.mapResponseToShoppingList(it) }

    override suspend fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean) =
        shoppingListDatabaseService.updateShopItem(id, name, quantity, checked)
            .let { shoppingListMapper.mapResponseToShopItem(it) }

    override suspend fun deleteShoppingList(id: Int) = shoppingListDatabaseService.deleteShoppingList(id)

    override suspend fun deleteShopItem(id: Int) = shoppingListDatabaseService.deleteShopItem(id)
}
