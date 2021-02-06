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

  companion object {
    private const val SHOPPING_LISTS_JSON = "[{\"id\": 1, \"name\": \"Sample List 1\"}, {\"id\": 2, \"name\": \"Sample List 2\"}]"
  }

}
