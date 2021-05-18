package com.ericthecoder.shopshopshoppinglist.dataaccess.repository

import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericthecoder.shopshopshoppinglist.entities.network.ShoppingListResponse

interface ShoppingListMapper {

  fun mapResponseToShoppingList(response: ShoppingListResponse): ShoppingList

  fun mapResponseToShopItem(response: ShopItemResponse): ShopItem

}
