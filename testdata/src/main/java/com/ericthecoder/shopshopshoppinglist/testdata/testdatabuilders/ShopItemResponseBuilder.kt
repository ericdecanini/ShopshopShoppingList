package com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders

import com.ericthecoder.shopshopshoppinglist.entities.network.ShopItemResponse

class ShopItemResponseBuilder private constructor() {

  private var id = DEFAULT_ID
  private var name = DEFAULT_NAME
  private var quantity = DEFAULT_QUANTITY
  private var isChecked = DEFAULT_CHECKED

  fun withId(id: Int) = apply {
    this.id = id
  }

  fun withName(name: String) = apply {
    this.name = name
  }

  fun withQuantity(quantity: Int) = apply {
    this.quantity = quantity
  }

  fun withChecked(isChecked: Int) = apply {
    this.isChecked = isChecked
  }

  fun build() = ShopItemResponse(
      id,
      name,
      quantity,
      isChecked
  )

  companion object {
    private const val DEFAULT_ID = 1
    private const val DEFAULT_NAME = "name"
    private const val DEFAULT_QUANTITY = 1
    private const val DEFAULT_CHECKED = 0

    fun aShopItemResponse() = ShopItemResponseBuilder()
  }
}
