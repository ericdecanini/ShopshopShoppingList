package com.ericdecanini.shopshopshoppinglist.dataaccess.repository

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShopItemBuilder.Companion.aShopItem
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShopItemResponseBuilder.Companion.aShopItemResponse
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.Companion.aShoppingList
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListResponseBuilder.Companion.aShoppingListResponse
import com.ericdecanini.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class SQLiteShoppingListRepositoryTest {

  private val shoppingListDatabaseService: ShoppingListDatabaseService = mock()
  private val shoppingListMapper: ShoppingListMapper = mock()
  private val shoppingListRepository = SQLiteShoppingListRepository(shoppingListDatabaseService, shoppingListMapper)

  private val sampleShoppingListResponse = aShoppingListResponse().build()
  private val sampleShoppingList = aShoppingList().build()
  private val sampleShopItemResponse = aShopItemResponse().build()
  private val sampleShopItem = aShopItem().build()

  @Before
  fun setUp() {
    given(shoppingListMapper.mapResponseToShoppingList(sampleShoppingListResponse)).willReturn(sampleShoppingList)
    given(shoppingListMapper.mapResponseToShopItem(sampleShopItemResponse)).willReturn(sampleShopItem)
  }

  @Test
  fun givenServiceReturnsResponse_whenGetShoppingLists_thenReturnMappedShoppingLists() {
    given(shoppingListDatabaseService.getShoppingLists()).willReturn(listOf(sampleShoppingListResponse))

    val shoppingLists = shoppingListRepository.getShoppingLists()

    assertThat(shoppingLists).isEqualTo(listOf(sampleShoppingList))
  }

  @Test
  fun givenServiceReturnsNull_whenGetShoppingLists_thenReturnEmptyList() {
    given(shoppingListDatabaseService.getShoppingLists()).willReturn(null)

    val shoppingLists = shoppingListRepository.getShoppingLists()

    assertThat(shoppingLists).isEqualTo(emptyList<ShoppingList>())
    verifyZeroInteractions(shoppingListMapper)
  }

  @Test
  fun givenServiceReturnsResponse_whenGetShoppingListById_thenReturnMappedShoppingList() {
    val id = 1
    given(shoppingListDatabaseService.getShoppingListById(id)).willReturn(sampleShoppingListResponse)

    val shoppingList = shoppingListRepository.getShoppingListById(id)

    assertThat(shoppingList).isEqualTo(sampleShoppingList)
  }

  @Test
  fun givenServiceReturnsNull_whenGetShoppingListById_thenReturnNull() {
    val id = 1
    given(shoppingListDatabaseService.getShoppingListById(id)).willReturn(null)

    val shoppingList = shoppingListRepository.getShoppingListById(id)

    assertThat(shoppingList).isNull()
  }

  @Test
  fun givenServiceReturnsResponse_whenCreateNewShoppingList_thenReturnCreatedShoppingList() {
    val name = "list_name"
    given(shoppingListDatabaseService.createShoppingList(name)).willReturn(sampleShoppingListResponse)

    val shoppingList = shoppingListRepository.createNewShoppingList(name)

    assertThat(shoppingList).isEqualTo(sampleShoppingList)
  }

  @Test
  fun givenServiceReturnsResponse_whenCreateNewShopItem_thenReturnCreatedShopItem() {
    val listId = 1
    val name = "item_name"
    given(shoppingListDatabaseService.createShopItem(listId, name)).willReturn(sampleShopItemResponse)

    val shopItem = shoppingListRepository.createNewShopItem(listId, name)

    assertThat(shopItem).isEqualTo(sampleShopItem)
  }

  @Test
  fun givenServiceReturnsResponse_whenUpdateShoppingList_thenReturnUpdatedShoppingList() {
    val id = 1
    val name = "list_name"
    given(shoppingListDatabaseService.updateShoppingList(id, name)).willReturn(sampleShoppingListResponse)

    val shoppingList = shoppingListRepository.updateShoppingList(id, name)

    assertThat(shoppingList).isEqualTo(sampleShoppingList)
  }

  @Test
  fun givenServiceReturnsNull_whenUpdateShoppingList_thenReturnNull() {
    val id = 1
    val name = "list_name"
    given(shoppingListDatabaseService.updateShoppingList(id, name)).willReturn(null)

    val shoppingList = shoppingListRepository.updateShoppingList(id, name)

    assertThat(shoppingList).isNull()
  }

  @Test
  fun givenServiceReturnsResponse_whenUpdateShopItem_thenReturnUpdatedShopItem() {
    val id = 1
    val name = "item_name"
    val quantity = 5
    val checked = false
    given(shoppingListDatabaseService.updateShopItem(id, name, quantity, checked)).willReturn(sampleShopItemResponse)

    val shopItem = shoppingListRepository.updateShopItem(id, name, quantity, checked)

    assertThat(shopItem).isEqualTo(sampleShopItem)
  }

  @Test
  fun givenServiceReturnsNull_whenUpdateShopItem_thenReturnNull() {
    val id = 1
    val name = "item_name"
    val quantity = 5
    val checked = false
    given(shoppingListDatabaseService.updateShopItem(id, name, quantity, checked)).willReturn(null)

    val shopItem = shoppingListRepository.updateShopItem(id, name, quantity, checked)

    assertThat(shopItem).isNull()
  }

  @Test
  fun givenId_whenDeleteShoppingList_thenDatabaseDeletesShoppingList() {
    val id = 1

    shoppingListRepository.deleteShoppingList(id)

    verify(shoppingListDatabaseService).deleteShoppingList(id)
  }

  @Test
  fun givenId_whenDeleteShopItem_thenDatabaseDeletesShopItem() {
    val id = 1

    shoppingListRepository.deleteShopItem(id)

    verify(shoppingListDatabaseService).deleteShopItem(id)
  }
}
