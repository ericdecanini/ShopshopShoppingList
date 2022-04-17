package com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders

import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

object ShoppingListBuilder {

    fun aShoppingList(
        id: Int = 0,
        name: String = "",
        items: MutableList<ShopItem> = mutableListOf(),
    ) = ShoppingList(
        id,
        name,
        items,
    )
}
