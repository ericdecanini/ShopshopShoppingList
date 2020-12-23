package com.ericdecanini.testdata.testdatabuilders

import com.ericdecanini.entities.ShopItem

class ShopItemBuilder private constructor() {

    private var name = DEFAULT_NAME
    private var quantity = DEFAULT_QUANTITY
    private var isChecked = DEFAULT_CHECKED

    fun withName(name: String) = apply {
        this.name = name
    }

    fun withQuantity(quantity: Int) = apply {
        this.quantity = quantity
    }

    fun withChecked(isChecked: Boolean) = apply {
        this.isChecked = isChecked
    }

    fun build() = ShopItem(
        name,
        quantity,
        isChecked
    )

    companion object {
        private const val DEFAULT_NAME = "name"
        private const val DEFAULT_QUANTITY = 1
        private const val DEFAULT_CHECKED = false

        fun aShopItem() = ShopItemBuilder()
    }

}
