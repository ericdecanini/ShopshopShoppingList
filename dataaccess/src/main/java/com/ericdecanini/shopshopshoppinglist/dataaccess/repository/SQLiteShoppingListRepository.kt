package com.ericdecanini.shopshopshoppinglist.dataaccess.repository

import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService

class SQLiteShoppingListRepository(
    private val shoppingListDatabaseService: ShoppingListDatabaseService,
    private val shoppingListMapper: ShoppingListMapper
) : ShoppingListRepository {

    override fun getShoppingLists()
        = shoppingListDatabaseService.getShoppingLists()
        ?.map { shoppingListMapper.mapResponseToShoppingList(it) }
        ?: emptyList()

    override fun getShoppingListById(id: Int)
        = shoppingListDatabaseService.getShoppingListById(id)
        ?.let { shoppingListMapper.mapResponseToShoppingList(it) }

    override fun createNewShoppingList(name: String)
        = shoppingListDatabaseService.createShoppingList(name)
        .let { shoppingListMapper.mapResponseToShoppingList(it) }

    override fun createNewShopItem(listId: Int, name: String)
        = shoppingListDatabaseService.createShopItem(listId, name)
        .let { shoppingListMapper.mapResponseToShopItem(it) }

    override fun updateShoppingList(id: Int, name: String)
        = shoppingListDatabaseService.updateShoppingList(id, name)
        ?.let { shoppingListMapper.mapResponseToShoppingList(it) }

    override fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean)
        = shoppingListDatabaseService.updateShopItem(id, name, quantity, checked)
        ?.let { shoppingListMapper.mapResponseToShopItem(it) }

    override fun deleteShoppingList(id: Int) = shoppingListDatabaseService.deleteShoppingList(id)

    override fun deleteShopItem(id: Int) = shoppingListDatabaseService.deleteShopItem(id)
}
