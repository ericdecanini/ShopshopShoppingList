package com.ericdecanini.shopshopshoppinglist.dataaccess.repository

import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericdecanini.shopshopshoppinglist.entities.network.ShoppingListResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class SQLiteShoppingListMapperTest {

  private val shoppingListMapper = SQLiteShoppingListMapper()

  @Test
  fun givenShoppingListResponse_whenConvertToShoppingList_thenConvertedCorrectly() {
    val id = 1
    val name = "list_name"
    val response = ShoppingListResponse(id, name)

    val shoppingList = shoppingListMapper.mapResponseToShoppingList(response)

    val expectedShoppingList = ShoppingList(id, name, mutableListOf())
    assertThat(shoppingList).isEqualTo(expectedShoppingList)
  }

  @Test
  fun givenShopItemResponseWithChecked0_whenConvertToShopItem_thenConvertedCorrectly() {
    val id = 1
    val name = "item_name"
    val quantity = 5
    val checked = 0
    val response = ShopItemResponse(id, name, quantity, checked)

    val shopItem = shoppingListMapper.mapResponseToShopItem(response)

    val expectedShopItem = ShopItem(id, name, quantity, false)
    assertThat(shopItem).isEqualTo(expectedShopItem)
  }

  @Test
  fun givenShopItemResponseWithChecked1_whenConvertToShopItem_thenConvertedCorrectly() {
    val id = 1
    val name = "item_name"
    val quantity = 5
    val checked = 1
    val response = ShopItemResponse(id, name, quantity, checked)

    val shopItem = shoppingListMapper.mapResponseToShopItem(response)

    val expectedShopItem = ShopItem(id, name, quantity, true)
    assertThat(shopItem).isEqualTo(expectedShopItem)
  }

  @Test
  fun givenShopItemResponseWithInvalidChecked_whenConvertToShopItem_thenThrowIllegalArgumentException() {
    val id = 1
    val name = "item_name"
    val quantity = 5
    val checked = 3
    val response = ShopItemResponse(id, name, quantity, checked)

    assertThrows<IllegalArgumentException> { shoppingListMapper.mapResponseToShopItem(response) }
  }
}
