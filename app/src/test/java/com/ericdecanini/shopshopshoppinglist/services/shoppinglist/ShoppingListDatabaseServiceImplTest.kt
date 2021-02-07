package com.ericdecanini.shopshopshoppinglist.services.shoppinglist

import com.ericdecanini.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericdecanini.shopshopshoppinglist.entities.network.ShoppingListResponse
import com.ericdecanini.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class ShoppingListDatabaseServiceImplTest {

  private val pythonDatabaseWrapper: PythonDatabaseWrapper = mock()
  private val shoppingListDatabaseService = ShoppingListDatabaseServiceImpl(pythonDatabaseWrapper)

  @Test
  fun givenDbHasShoppingLists_whenGetShoppingLists_thenReturnJson() {
    given(pythonDatabaseWrapper.getShoppingListsJson()).willReturn(SAMPLE_SHOPPING_LISTS_JSON)

    val shoppingLists = shoppingListDatabaseService.getShoppingLists()

    assertThat(shoppingLists).isEqualTo(SAMPLE_SHOPPING_LISTS)
  }

  @Test
  fun givenDbHasShoppingListWithId_whenGetShoppingListsById_thenReturnJson() {
    val id = 1
    given(pythonDatabaseWrapper.getShoppingListJsonById(id)).willReturn(SAMPLE_SINGLE_SHOPPING_LIST_JSON)

    val shoppingList = shoppingListDatabaseService.getShoppingListById(id)

    assertThat(shoppingList).isEqualTo(SAMPLE_SINGLE_SHOPPING_LIST)
  }

  @Test
  fun givenDbInsertIsSuccessful_whenCreateShoppingList_thenShoppingListIsCreated() {
    val name = "sample_name"
    given(pythonDatabaseWrapper.insertShoppingList(name)).willReturn(SAMPLE_SINGLE_SHOPPING_LIST_JSON)

    val shoppingList = shoppingListDatabaseService.createShoppingList(name)

    assertThat(shoppingList).isEqualTo(SAMPLE_SINGLE_SHOPPING_LIST)
  }

  @Test
  fun givenDbInsertIsNotSuccessful_whenCreateShoppingList_thenThrowIllegalStateException() {
    val name = "sample_name"
    given(pythonDatabaseWrapper.insertShoppingList(name)).willReturn(EMPTY_OBJECT_RESPONSE)

    assertThrows<IllegalStateException> { shoppingListDatabaseService.createShoppingList(name) }
  }

  @Test
  fun givenDbInsertIsSuccessful_whenCreateShopItem_thenShopItemIsCreated() {
    given(pythonDatabaseWrapper.insertShopItem(any(), any(), eq(1), eq(false))).willReturn(SAMPLE_SHOPITEM_JSON)

    val shopItem = shoppingListDatabaseService.createShopItem(0, "name")

    assertThat(shopItem).isEqualTo(SAMPLE_SHOPITEM)
  }

  @Test
  fun givenDbInsertIsNotSuccessful_whenCreateShopItem_thenShopItemIsCreated() {
    given(pythonDatabaseWrapper.insertShopItem(any(), any(), eq(1), eq(false))).willReturn(EMPTY_OBJECT_RESPONSE)

    assertThrows<IllegalStateException> { shoppingListDatabaseService.createShopItem(0, "name") }
  }

  @Test
  fun givenDbHasShoppingList_whenUpdateShoppingList_thenReturnUpdatedShoppingList() {
    given(pythonDatabaseWrapper.updateShoppingList(any(), any())).willReturn(SAMPLE_SINGLE_SHOPPING_LIST_JSON)

    val shoppingList = shoppingListDatabaseService.updateShoppingList(1, "name")

    assertThat(shoppingList).isEqualTo(SAMPLE_SINGLE_SHOPPING_LIST)
  }

  @Test
  fun givenDbDoesNotHaveShoppingList_whenUpdateShoppingList_thenReturnNull() {
    given(pythonDatabaseWrapper.updateShoppingList(any(), any())).willReturn(EMPTY_OBJECT_RESPONSE)

    val shoppingList = shoppingListDatabaseService.updateShoppingList(1, "name")

    assertThat(shoppingList).isNull()
  }

  @Test
  fun givenDbHasShopItem_whenUpdateShopItem_thenReturnUpdatedShopItem() {
    given(pythonDatabaseWrapper.updateShopItem(any(), any(), any(), any())).willReturn(SAMPLE_SHOPITEM_JSON)

    val shopItem = shoppingListDatabaseService.updateShopItem(1, "name", 1, false)

    assertThat(shopItem).isEqualTo(SAMPLE_SHOPITEM)
  }

  @Test
  fun givenDbDoesNotHaveShopItem_whenUpdateShopItem_thenReturnNull() {
    given(pythonDatabaseWrapper.updateShopItem(any(), any(), any(), any())).willReturn(EMPTY_OBJECT_RESPONSE)

    val shopItem = shoppingListDatabaseService.updateShopItem(1, "name", 1, false)

    assertThat(shopItem).isNull()
  }

  @Test
  fun whenDeleteShoppingList_thenDatabaseDeletesShoppingList() {
    val id = 1

    shoppingListDatabaseService.deleteShoppingList(id)

    verify(pythonDatabaseWrapper).deleteShoppingList(id)
  }

  @Test
  fun whenDeleteShopItem_thenDatabaseDeletesShopItem() {
    val id = 1

    shoppingListDatabaseService.deleteShopItem(id)

    verify(pythonDatabaseWrapper).deleteShopItem(id)
  }

  companion object {
    private const val SAMPLE_SHOPPING_LISTS_JSON = "[{\"id\": 1, \"name\": \"Sample List 1\"}, {\"id\": 2, \"name\": \"Sample List 2\"}]"
    private const val SAMPLE_SINGLE_SHOPPING_LIST_JSON = "{\"id\": 1, \"name\": \"Sample List\"}"
    private const val SAMPLE_SHOPITEM_JSON = "{\"id\": 1, \"list_id\": 1, \"name\": \"Sample Item\", \"quantity\": 1, \"checked\": 0}"
    private const val EMPTY_OBJECT_RESPONSE = "null"

    private val SAMPLE_SHOPPING_LISTS = listOf(ShoppingListResponse(1, "Sample List 1"), ShoppingListResponse(2, "Sample List 2"))
    private val SAMPLE_SINGLE_SHOPPING_LIST = ShoppingListResponse(1, "Sample List")
    private val SAMPLE_SHOPITEM = ShopItemResponse(1, "Sample Item", 1, 0)
  }

}
