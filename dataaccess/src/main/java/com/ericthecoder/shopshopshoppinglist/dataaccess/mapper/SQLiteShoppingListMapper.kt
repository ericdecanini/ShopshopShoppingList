package com.ericthecoder.shopshopshoppinglist.dataaccess.mapper

import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericthecoder.shopshopshoppinglist.entities.network.ShoppingListResponse

class SQLiteShoppingListMapper : ShoppingListMapper {

  override fun mapResponseToShoppingList(response: ShoppingListResponse) = with(response) {
    ShoppingList(
        id,
        name,
        response.items.map { mapResponseToShopItem(it) }.toMutableList()
    )
  }

  override fun mapResponseToShopItem(response: ShopItemResponse) = with(response) {
    val checkedBoolean = when(checked) {
      0 -> false
      1 -> true
      else -> throw IllegalArgumentException("$INVALID_CHECKED_MESSAGE, checked = $checked")
    }

    ShopItem(id, name, quantity, checkedBoolean)
  }

  companion object {
    private const val INVALID_CHECKED_MESSAGE = "CHECKED should be either 0 or 1"
  }
}
