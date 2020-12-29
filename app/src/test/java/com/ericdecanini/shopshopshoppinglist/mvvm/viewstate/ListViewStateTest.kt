package com.ericdecanini.shopshopshoppinglist.mvvm.viewstate

import com.ericdecanini.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list.ListViewState
import com.ericdecanini.testdata.testdatabuilders.ShopItemBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ListViewStateTest {

    @Test
    fun givenStateHasEmptyList_whenAddItem_thenListHasOneItem() {
        val state =
            ListViewState(
                list = emptyList()
            )
        val shopItem = ShopItemBuilder.aShopItem().build()

        val newState = state.addItem(shopItem)

        val expectedState =
            ListViewState(
                list = listOf(shopItem)
            )
        assertThat(newState).isEqualTo(expectedState)
    }

    @Test
    fun givenStateHasListWithItems_whenAddItem_thenListHasAllItems() {
        val shopItems = listOf(
            ShopItemBuilder.aShopItem().withName("item1").build(),
            ShopItemBuilder.aShopItem().withName("item2").build()
        )
        val state =
            ListViewState(
                list = shopItems
            )
        val newShopItem = ShopItemBuilder.aShopItem().withName("item3").build()

        val newState = state.addItem(newShopItem)

        val expectedState =
            ListViewState(
                list = shopItems.toMutableList().apply { add(newShopItem) }
            )
        assertThat(newState).isEqualTo(expectedState)
    }

    @Test
    fun givenStateHasNoItems_whenReplaceItem_thenReturnStateUnchanged() {
        val state =
            ListViewState(
                list = emptyList()
            )
        val shopItem = ShopItem.newItem("")

        val newState = state.replaceItem(shopItem, shopItem)

        assertThat(newState).isEqualTo(state)
    }

    @Test
    fun givenStateHasListWithItems_whenReplaceItem_thenItemReplaced() {
        val item1 =  ShopItemBuilder.aShopItem().withName("item1").build()
        val item2 = ShopItemBuilder.aShopItem().withName("item2").build()
        val shopItems = listOf(item1, item2)
        val state =
            ListViewState(
                list = shopItems
            )
        val newItem2 = ShopItemBuilder.aShopItem().withName("newItem2").build()

        val newState = state.replaceItem(item2, newItem2)

        val expectedState =
            ListViewState(
                list = shopItems.toMutableList().apply { set(indexOf(item2), newItem2) }
            )
        assertThat(newState).isEqualTo(expectedState)
    }

    @Test
    fun givenStateHasListWithItems_whenDeleteItem_thenItemDeleted() {
        val item1 =  ShopItemBuilder.aShopItem().withName("item1").build()
        val item2 = ShopItemBuilder.aShopItem().withName("item2").build()
        val shopItems = listOf(item1, item2)
        val state =
            ListViewState(
                list = shopItems
            )

        val newState = state.deleteItem(item1)

        val expectedState =
            ListViewState(
                list = shopItems.toMutableList().apply { remove(item1) }
            )
        assertThat(newState).isEqualTo(expectedState)
    }

}
