package com.ericthecoder.dependencies.android.room.service

import com.ericthecoder.dependencies.android.room.dao.ShoppingListDao
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService

class RoomShoppingListDatabaseService(
    private val dao: ShoppingListDao
) : ShoppingListDatabaseService {

    override suspend fun getShoppingLists(): List<ShoppingList> {
        TODO("Not yet implemented")
    }

    override suspend fun getShoppingListById(id: Int): ShoppingList {
        TODO("Not yet implemented")
    }

    override suspend fun createShoppingList(name: String): ShoppingList {
        TODO("Not yet implemented")
    }

    override suspend fun createShopItem(listId: Int, name: String): ShopItem {
        TODO("Not yet implemented")
    }

    override suspend fun updateShoppingList(id: Int, name: String): ShoppingList {
        TODO("Not yet implemented")
    }

    override suspend fun updateShopItem(currentName: String, newName: String, quantity: Int, checked: Boolean): ShopItem {
        TODO("Not yet implemented")
    }

    override suspend fun deleteShoppingList(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteShopItem(name: String) {
        TODO("Not yet implemented")
    }
}