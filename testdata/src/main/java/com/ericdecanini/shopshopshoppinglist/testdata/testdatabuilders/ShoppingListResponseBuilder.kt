package com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders

import com.ericdecanini.shopshopshoppinglist.entities.network.ShoppingListResponse

class ShoppingListResponseBuilder private constructor() {

  private var id: Int = DEFAULT_ID
  private var title: String = DEFAULT_TITLE

  fun build() = ShoppingListResponse(
      id,
      title
  )

  fun withId(id: Int) = apply {
    this.id = id
  }

  fun withTitle(title: String) = apply {
    this.title = title
  }

  companion object {
    private const val DEFAULT_ID = 0
    private const val DEFAULT_TITLE = "title"

    fun aShoppingListResponse() = ShoppingListResponseBuilder()
  }

}
