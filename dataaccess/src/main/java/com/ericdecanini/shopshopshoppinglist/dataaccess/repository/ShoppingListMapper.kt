package com.ericdecanini.shopshopshoppinglist.dataaccess.repository

import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericdecanini.shopshopshoppinglist.entities.network.ShoppingListResponse

interface ShoppingListMapper {

  fun mapResponseToShoppingList(response: ShoppingListResponse): ShoppingList

  fun mapResponseToShopItem(response: ShopItemResponse): ShopItem

}
