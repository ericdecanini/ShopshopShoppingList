package com.ericthecoder.dependencies.android.room.testbuilders

import com.ericthecoder.dependencies.android.room.entity.ShoppingListEntity
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem

object ShoppingListEntityBuilder {

    fun aShoppingListEntity(
        id: Int = 0,
        name: String = "",
        items: List<ShopItem> = emptyList(),
    ) = ShoppingListEntity(
        id,
        name,
        items,
    )
}