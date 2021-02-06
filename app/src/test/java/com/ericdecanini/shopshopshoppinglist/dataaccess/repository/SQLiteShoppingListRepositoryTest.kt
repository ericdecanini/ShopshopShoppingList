package com.ericdecanini.shopshopshoppinglist.dataaccess.repository

import com.ericdecanini.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class SQLiteShoppingListRepositoryTest {

  private val shoppingListDatabaseService: ShoppingListDatabaseService = mock()
  private val shoppingListRepository = SQLiteShoppingListRepository(shoppingListDatabaseService)

  @Test
  fun whenGetShoppingLists_thenGetShoppingListsFromDatabaseService() {

    shoppingListRepository.getShoppingLists()

    verify(shoppingListDatabaseService).getShoppingLists()
  }

  @Test
  fun givenId_whenGetShoppingListById_thenGetShoppingListFromDatabaseService() {
    val id = 69

    shoppingListRepository.getShoppingListById(id)

    verify(shoppingListDatabaseService).getShoppingListById(id)
  }

}
