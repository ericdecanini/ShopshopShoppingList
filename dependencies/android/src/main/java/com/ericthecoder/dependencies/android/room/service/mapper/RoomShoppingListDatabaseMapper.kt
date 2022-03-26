package com.ericthecoder.dependencies.android.room.service.mapper

import com.ericthecoder.dependencies.android.room.entity.ShoppingListEntity
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

interface RoomShoppingListDatabaseMapper {

    fun mapEntityToShoppingList(entity: ShoppingListEntity): ShoppingList

    fun mapShoppingListToEntity(shoppingList: ShoppingList): ShoppingListEntity
}