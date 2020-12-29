package com.ericdecanini.testdata.testdatabuilders

import com.ericdecanini.entities.ShopItem
import com.ericdecanini.entities.ShoppingList

class ShoppingListBuilder private constructor() {

    private var id: Int = DEFAULT_ID
    private var title: String = DEFAULT_TITLE
    private var items: List<ShopItem> = DEFAULT_ITEMS

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

    fun withItems(items: List<ShopItem>) = apply {
        this.items = items
    }

    companion object {
        private const val DEFAULT_ID = 0
        private const val DEFAULT_TITLE = "title"
        private val DEFAULT_ITEMS = listOf<ShopItem>()

        fun aShoppingList() = ShoppingListBuilder()
    }
}
