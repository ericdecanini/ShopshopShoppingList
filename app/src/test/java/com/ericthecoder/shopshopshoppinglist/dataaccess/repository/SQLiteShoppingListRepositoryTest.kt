package com.ericthecoder.shopshopshoppinglist.dataaccess.repository

import com.ericthecoder.shopshopshoppinglist.dataaccess.mapper.ShoppingListMapper
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShopItemBuilder.Companion.aShopItem
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShopItemResponseBuilder.Companion.aShopItemResponse
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.Companion.aShoppingList
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListResponseBuilder.Companion.aShoppingListResponse
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
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
  fun givenServiceReturnsResponse_whenGetShoppingLists_thenReturnMappedShoppingLists() = runBlockingTest {
    given(shoppingListDatabaseService.getShoppingLists()).willReturn(listOf(sampleShoppingListResponse))

    val shoppingLists = shoppingListRepository.getShoppingLists()

    assertThat(shoppingLists).isEqualTo(listOf(sampleShoppingList))
  }

  @Test
  fun givenServiceReturnsEmpty_whenGetShoppingLists_thenReturnEmptyList() = runBlockingTest {
    given(shoppingListDatabaseService.getShoppingLists()).willReturn(emptyList())

    val shoppingLists = shoppingListRepository.getShoppingLists()

    assertThat(shoppingLists).isEqualTo(emptyList<ShoppingList>())
    verifyZeroInteractions(shoppingListMapper)
  }

  @Test
  fun givenServiceReturnsResponse_whenGetShoppingListById_thenReturnMappedShoppingList() = runBlockingTest {
    val id = 1
    given(shoppingListDatabaseService.getShoppingListById(id)).willReturn(sampleShoppingListResponse)

    val shoppingList = shoppingListRepository.getShoppingListById(id)

    assertThat(shoppingList).isEqualTo(sampleShoppingList)
  }

  @Test
  fun givenServiceReturnsNull_whenGetShoppingListById_thenReturnNull() = runBlockingTest {
    val id = 1
    given(shoppingListDatabaseService.getShoppingListById(id)).willReturn(null)

    val shoppingList = shoppingListRepository.getShoppingListById(id)

    assertThat(shoppingList).isNull()
  }

  @Test
  fun givenServiceReturnsResponse_whenCreateNewShoppingList_thenReturnCreatedShoppingList() = runBlockingTest {
    val name = "list_name"
    given(shoppingListDatabaseService.createShoppingList(name)).willReturn(sampleShoppingListResponse)

    val shoppingList = shoppingListRepository.createNewShoppingList(name)

    assertThat(shoppingList).isEqualTo(sampleShoppingList)
  }

  @Test
  fun givenServiceReturnsResponse_whenCreateNewShopItem_thenReturnCreatedShopItem() = runBlockingTest {
    val listId = 1
    val name = "item_name"
    given(shoppingListDatabaseService.createShopItem(listId, name)).willReturn(sampleShopItemResponse)

    val shopItem = shoppingListRepository.createNewShopItem(listId, name)

    assertThat(shopItem).isEqualTo(sampleShopItem)
  }

  @Test
  fun givenServiceReturnsResponse_whenUpdateShoppingList_thenReturnUpdatedShoppingList() = runBlockingTest {
    val id = 1
    val name = "list_name"
    given(shoppingListDatabaseService.updateShoppingList(id, name)).willReturn(sampleShoppingListResponse)

    val shoppingList = shoppingListRepository.updateShoppingList(id, name)

    assertThat(shoppingList).isEqualTo(sampleShoppingList)
  }

  @Test
  fun givenServiceReturnsNull_whenUpdateShoppingList_thenReturnNull() = runBlockingTest {
    val id = 1
    val name = "list_name"
    given(shoppingListDatabaseService.updateShoppingList(id, name)).willReturn(null)

    val shoppingList = shoppingListRepository.updateShoppingList(id, name)

    assertThat(shoppingList).isNull()
  }

  @Test
  fun givenServiceReturnsResponse_whenUpdateShopItem_thenReturnUpdatedShopItem() = runBlockingTest {
    val name = "item_name"
    val quantity = 5
    val checked = false
    given(shoppingListDatabaseService.updateShopItem(name, name, quantity, checked)).willReturn(sampleShopItemResponse)

    val shopItem = shoppingListRepository.updateShopItem(name, name, quantity, checked)

    assertThat(shopItem).isEqualTo(sampleShopItem)
  }

  @Test
  fun givenServiceReturnsNull_whenUpdateShopItem_thenReturnNull() = runBlockingTest {
    val name = "item_name"
    val quantity = 5
    val checked = false
    given(shoppingListDatabaseService.updateShopItem(name, name, quantity, checked)).willReturn(null)

    val shopItem = shoppingListRepository.updateShopItem(name, name, quantity, checked)

    assertThat(shopItem).isNull()
  }

  @Test
  fun givenId_whenDeleteShoppingList_thenDatabaseDeletesShoppingList() = runBlockingTest {
    val id = 1

    shoppingListRepository.deleteShoppingList(id)

    verify(shoppingListDatabaseService).deleteShoppingList(id)
  }

  @Test
  fun givenName_whenDeleteShopItem_thenDatabaseDeletesShopItem() = runBlockingTest {
    val name = "item_name"

    shoppingListRepository.deleteShopItem(name)

    verify(shoppingListDatabaseService).deleteShopItem(name)
  }
}
