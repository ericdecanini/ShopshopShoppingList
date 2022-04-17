package com.ericthecoder.dependencies.android.room.service

import com.ericthecoder.dependencies.android.room.dao.ShoppingListDao
import com.ericthecoder.dependencies.android.room.entity.ShoppingListEntity
import com.ericthecoder.dependencies.android.room.service.mapper.RoomShoppingListDatabaseMapper
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService

class RoomShoppingListDatabaseService(
    private val dao: ShoppingListDao,
    private val mapper: RoomShoppingListDatabaseMapper,
) : ShoppingListDatabaseService {

    override suspend fun getShoppingLists() = dao.getAll()
        .map { mapper.mapEntityToShoppingList(it) }

    override suspend fun getShoppingListById(id: Int) = dao.getById(id)
        .let { mapper.mapEntityToShoppingList(it) }

    override suspend fun createShoppingList(name: String): Int {
        val entity = ShoppingListEntity(IRRELEVANT, name, emptyList())
        return dao.insert(entity).toInt()
    }

    override suspend fun updateShoppingList(shoppingList: ShoppingList) {
        val entity = mapper.mapShoppingListToEntity(shoppingList)
        dao.update(entity)
    }

    override suspend fun deleteShoppingList(id: Int) {
        dao.deleteById(id)
    }

    companion object {
        internal const val IRRELEVANT = 0
    }
}