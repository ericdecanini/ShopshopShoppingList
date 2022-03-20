package com.ericthecoder.shopshopshoppinglist.services.shoppinglist

import com.ericthecoder.shopshopshoppinglist.entities.database.DbQueryFailedException
import com.ericthecoder.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericthecoder.shopshopshoppinglist.entities.network.ShoppingListResponse
import com.ericthecoder.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class ShoppingListDatabaseServiceImplTest {

  private val pythonDatabaseWrapper: PythonDatabaseWrapper = mock()
  private val shoppingListDatabaseService = ShoppingListDatabaseServiceImpl(pythonDatabaseWrapper)

  @Test
  fun givenDbHasShoppingLists_whenGetShoppingLists_thenReturnShoppingLists() = runBlockingTest {
    given(pythonDatabaseWrapper.getShoppingListsJson()).willReturn(SAMPLE_SHOPPING_LISTS_JSON)

    val shoppingLists = shoppingListDatabaseService.getShoppingLists()

    assertThat(shoppingLists).isEqualTo(SAMPLE_SHOPPING_LISTS)
  }

  @Test
  fun givenDbHasShoppingListWithId_whenGetShoppingListsById_thenReturnShoppingList() = runBlockingTest {
    val id = 1
    given(pythonDatabaseWrapper.getShoppingListJsonById(id)).willReturn(SAMPLE_SINGLE_SHOPPING_LIST_JSON)

    val shoppingList = shoppingListDatabaseService.getShoppingListById(id)

    assertThat(shoppingList).isEqualTo(SAMPLE_SINGLE_SHOPPING_LIST)
  }

  @Test
  fun givenDbHasShoppingListWithNoItems_whenGetShoppingListsById_thenReturnShoppingList() = runBlockingTest {
    val id = 1
    given(pythonDatabaseWrapper.getShoppingListJsonById(id)).willReturn(SAMPLE_SINGLE_SHOPPING_LIST_NO_ITEMS_JSON)

    val shoppingList = shoppingListDatabaseService.getShoppingListById(id)

    assertThat(shoppingList).isEqualTo(SAMPLE_SINGLE_SHOPPING_LIST_NO_ITEMS)
  }

  @Test
  fun givenDbInsertIsSuccessful_whenCreateShoppingList_thenShoppingListIsCreated() = runBlockingTest {
    val name = "sample_name"
    given(pythonDatabaseWrapper.insertShoppingList(name)).willReturn(SAMPLE_SINGLE_SHOPPING_LIST_JSON)

    val shoppingList = shoppingListDatabaseService.createShoppingList(name)

    assertThat(shoppingList).isEqualTo(SAMPLE_SINGLE_SHOPPING_LIST)
  }

  @Test
  fun givenDbInsertIsNotSuccessful_whenCreateShoppingList_thenThrowIllegalStateException() = runBlockingTest {
    val name = "sample_name"
    given(pythonDatabaseWrapper.insertShoppingList(name)).willReturn(EMPTY_OBJECT_RESPONSE)

    assertThrows<DbQueryFailedException> {
      shoppingListDatabaseService.createShoppingList(name)
    }
  }

  @Test
  fun givenDbInsertIsSuccessful_whenCreateShopItem_thenShopItemIsCreated() = runBlockingTest {
    given(pythonDatabaseWrapper.insertShopItem(any(), any(), eq(1), eq(false))).willReturn(SAMPLE_SHOPITEM_JSON)

    val shopItem = shoppingListDatabaseService.createShopItem(0, "name")

    assertThat(shopItem).isEqualTo(SAMPLE_SHOPITEM)
  }

  @Test
  fun givenDbInsertIsNotSuccessful_whenCreateShopItem_thenRethrowException() = runBlockingTest {
    given(pythonDatabaseWrapper.insertShopItem(any(), any(), eq(1), eq(false))).willReturn(EMPTY_OBJECT_RESPONSE)

    assertThrows<DbQueryFailedException> {
      shoppingListDatabaseService.createShopItem(0, "name")
    }
  }

  @Test
  fun givenDbHasShoppingList_whenUpdateShoppingList_thenReturnUpdatedShoppingList() = runBlockingTest {
    given(pythonDatabaseWrapper.updateShoppingList(any(), any())).willReturn(SAMPLE_SINGLE_SHOPPING_LIST_JSON)

    val shoppingList = shoppingListDatabaseService.updateShoppingList(1, "name")

    assertThat(shoppingList).isEqualTo(SAMPLE_SINGLE_SHOPPING_LIST)
  }

  @Test
  fun givenDbDoesNotHaveShoppingList_whenUpdateShoppingList_thenReturnNull() = runBlockingTest {
    given(pythonDatabaseWrapper.updateShoppingList(any(), any())).willReturn(EMPTY_OBJECT_RESPONSE)

    assertThrows<DbQueryFailedException> {
      shoppingListDatabaseService.updateShoppingList(1, "name")
    }
  }

  @Test
  fun givenDbHasShopItem_whenUpdateShopItem_thenReturnUpdatedShopItem() = runBlockingTest {
    given(pythonDatabaseWrapper.updateShopItem(any(), any(), any(), any())).willReturn(SAMPLE_SHOPITEM_JSON)

    val shopItem = shoppingListDatabaseService.updateShopItem("name", "name", 1, false)

    assertThat(shopItem).isEqualTo(SAMPLE_SHOPITEM)
  }

  @Test
  fun givenDbDoesNotHaveShopItem_whenUpdateShopItem_thenThrow() = runBlockingTest {
    given(pythonDatabaseWrapper.updateShopItem(any(), any(), any(), any())).willReturn(EMPTY_OBJECT_RESPONSE)

    assertThrows<DbQueryFailedException> {
      shoppingListDatabaseService.updateShopItem("name", "name", 1, false)
    }
  }

  @Test
  fun whenDeleteShoppingList_thenDatabaseDeletesShoppingList() = runBlockingTest {
    val id = 1

    shoppingListDatabaseService.deleteShoppingList(id)

    verify(pythonDatabaseWrapper).deleteShoppingList(id)
  }

  @Test
  fun whenDeleteShopItem_thenDatabaseDeletesShopItem() = runBlockingTest {
    val name = "name"

    shoppingListDatabaseService.deleteShopItem(name)

    verify(pythonDatabaseWrapper).deleteShopItem(name)
  }

  companion object {
    private const val SAMPLE_SHOPPING_LISTS_JSON = "[{\"id\": 1, \"name\": \"Sample List 1\", \"items\": [{\"id\": 1, \"list_id\": 1, \"name\": \"Sample Item 1\", \"quantity\": 1, \"checked\": 0}, {\"id\": 2, \"list_id\": 1, \"name\": \"Sample Item 2\", \"quantity\": 1, \"checked\": 0}]}, {\"id\": 2, \"name\": \"Sample List 2\", \"items\": [{\"id\": 3, \"list_id\": 2, \"name\": \"Sample Item 1\", \"quantity\": 1, \"checked\": 0}, {\"id\": 4, \"list_id\": 2, \"name\": \"Sample Item 2\", \"quantity\": 1, \"checked\": 0}]}]"
    private const val SAMPLE_SINGLE_SHOPPING_LIST_JSON = "{\"id\": 1, \"name\": \"Sample List\", \"items\": [{\"id\": 1, \"list_id\": 1, \"name\": \"Sample Item 1\", \"quantity\": 1, \"checked\": 0}, {\"id\": 2, \"list_id\": 1, \"name\": \"Sample Item 2\", \"quantity\": 1, \"checked\": 0}]}"
    private const val SAMPLE_SINGLE_SHOPPING_LIST_NO_ITEMS_JSON = "{\"id\": 1, \"name\": \"Sample List\", \"items\": []}"
    private const val SAMPLE_SHOPITEM_JSON = "{\"id\": 1, \"list_id\": 1, \"name\": \"Sample Item\", \"quantity\": 1, \"checked\": 0}"
    private const val EMPTY_OBJECT_RESPONSE = "null"

    private val SAMPLE_ITEMS_LIST = listOf(
        ShopItemResponse(1, "Sample Item 1", 1, 0),
        ShopItemResponse(2, "Sample Item 2", 1, 0)
    )
    private val SAMPLE_ITEMS_LIST_2 = listOf(
        ShopItemResponse(3, "Sample Item 1", 1, 0),
        ShopItemResponse(4, "Sample Item 2", 1, 0)
    )
    private val SAMPLE_SHOPPING_LISTS = listOf(
        ShoppingListResponse(1, "Sample List 1", SAMPLE_ITEMS_LIST),
        ShoppingListResponse(2, "Sample List 2", SAMPLE_ITEMS_LIST_2)
    )
    private val SAMPLE_SINGLE_SHOPPING_LIST = ShoppingListResponse(1, "Sample List", SAMPLE_ITEMS_LIST)
    private val SAMPLE_SINGLE_SHOPPING_LIST_NO_ITEMS = ShoppingListResponse(1, "Sample List", listOf())
    private val SAMPLE_SHOPITEM = ShopItemResponse(1, "Sample Item", 1, 0)
  }

}
