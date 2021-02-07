package com.ericdecanini.shopshopshoppinglist.services.shoppinglist

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ShoppingListDatabaseServiceImplTest {

  private val pythonDatabaseWrapper: PythonDatabaseWrapper = mock()
  private val shoppingListDatabaseService = ShoppingListDatabaseServiceImpl(pythonDatabaseWrapper)

  @Test
  fun givenDbHasShoppingLists_whenGetShoppingLists_thenReturnJson() {
    given(pythonDatabaseWrapper.getShoppingListsJson()).willReturn(SHOPPING_LISTS_JSON)

    val shoppingLists = shoppingListDatabaseService.getShoppingLists()

    val expectedLists = listOf(
        ShoppingList(1, "Sample List 1"),
        ShoppingList(2, "Sample List 2")
    )
    assertThat(shoppingLists).isEqualTo(expectedLists)
  }

  @Test
  fun givenDbHasShoppingLists_whenGetShoppingListsById_thenReturnJson() {
    val id = 1
    given(pythonDatabaseWrapper.getShoppingListJsonById(id)).willReturn(SINGLE_SHOPPING_LIST_JSON)

    val shoppingList = shoppingListDatabaseService.getShoppingListById(id)

    val expectedList = ShoppingList(1, "Sample List")
    assertThat(shoppingList).isEqualTo(expectedList)
  }

  companion object {
    private const val SHOPPING_LISTS_JSON = "[{\"id\": 1, \"name\": \"Sample List 1\"}, {\"id\": 2, \"name\": \"Sample List 2\"}]"
    private const val SINGLE_SHOPPING_LIST_JSON = "{\"id\": 1, \"name\": \"Sample List\"}"
  }

}
