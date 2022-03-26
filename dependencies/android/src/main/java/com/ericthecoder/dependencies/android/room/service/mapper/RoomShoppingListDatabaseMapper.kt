package com.ericthecoder.dependencies.android.room.service.mapper

import com.ericthecoder.dependencies.android.room.entity.ShoppingListEntity
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

class RoomShoppingListDatabaseMapper {

    fun mapEntityToShoppingList(entity: ShoppingListEntity) =
        ShoppingList(
            entity.id,
            entity.name,
            entity.items.toMutableList(),
        )

    fun mapShoppingListToEntity(shoppingList: ShoppingList) =
        ShoppingListEntity(
            shoppingList.id,
            shoppingList.name,
            shoppingList.items,
        )
}
