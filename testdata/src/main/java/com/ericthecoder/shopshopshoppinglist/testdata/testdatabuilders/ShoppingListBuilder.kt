package com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders

import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

class ShoppingListBuilder private constructor() {

    private var id: Int = DEFAULT_ID
    private var title: String = DEFAULT_TITLE
    private var items: MutableList<ShopItem> = DEFAULT_ITEMS

    fun build() = ShoppingList(
        id,
        title,
        items
    )

    fun withId(id: Int) = apply {
        this.id = id
    }

    fun withTitle(title: String) = apply {
        this.title = title
    }

    fun withItems(items: MutableList<ShopItem>) = apply {
        this.items = items
    }

    companion object {
        private const val DEFAULT_ID = 0
        private const val DEFAULT_TITLE = "title"
        private val DEFAULT_ITEMS = mutableListOf<ShopItem>()

        fun aShoppingList() = ShoppingListBuilder()
    }
}
