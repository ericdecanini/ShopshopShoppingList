package com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders

import com.ericdecanini.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericdecanini.shopshopshoppinglist.entities.network.ShoppingListResponse

class ShoppingListResponseBuilder private constructor() {

  private var id: Int = DEFAULT_ID
  private var title: String = DEFAULT_TITLE
  private var items: List<ShopItemResponse> = DEFAULT_ITEMS

  fun build() = ShoppingListResponse(
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

  fun withItems(items: List<ShopItemResponse>) = apply {
    this.items = items
  }

  companion object {
    private const val DEFAULT_ID = 0
    private const val DEFAULT_TITLE = "title"
    private val DEFAULT_ITEMS = listOf<ShopItemResponse>()

    fun aShoppingListResponse() = ShoppingListResponseBuilder()
  }

}
