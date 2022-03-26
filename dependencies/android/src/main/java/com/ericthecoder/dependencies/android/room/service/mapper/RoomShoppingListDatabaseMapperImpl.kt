package com.ericthecoder.dependencies.android.room.service.mapper

import com.ericthecoder.dependencies.android.room.entity.ShoppingListEntity
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

class RoomShoppingListDatabaseMapperImpl : RoomShoppingListDatabaseMapper {

    override fun mapEntityToShoppingList(entity: ShoppingListEntity) =
        ShoppingList(
            entity.id,
            entity.name,
            entity.items.toMutableList(),
        )

    override fun mapShoppingListToEntity(shoppingList: ShoppingList) =
        ShoppingListEntity(
            shoppingList.id,
            shoppingList.name,
            shoppingList.items,
        )
}
