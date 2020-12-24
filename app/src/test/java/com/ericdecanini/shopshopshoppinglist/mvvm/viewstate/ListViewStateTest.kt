package com.ericdecanini.shopshopshoppinglist.mvvm.viewstate

import com.ericdecanini.entities.ShopItem
import com.ericdecanini.testdata.testdatabuilders.ShopItemBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ListViewStateTest {

    @Test
    fun givenStateHasEmptyList_whenAddNewItem_thenListHasOneItem() {
        val state = ListViewState(list = emptyList())
        val shopItem = ShopItemBuilder.aShopItem().build()

        val newState = state.addNewItem(shopItem)

        val expectedState = ListViewState(list = listOf(shopItem))
        assertThat(newState).isEqualTo(expectedState)
    }

    @Test
    fun givenStateHasListWithItems_whenAddNewItem_thenListHasOneItem() {
        val shopItems = listOf(
            ShopItemBuilder.aShopItem().withName("item1").build(),
            ShopItemBuilder.aShopItem().withName("item2").build()
        )
        val state = ListViewState(list = shopItems)
        val newShopItem = ShopItemBuilder.aShopItem().withName("item3").build()

        val newState = state.addNewItem(newShopItem)

        val expectedState = ListViewState(
            list = shopItems.toMutableList().apply { add(newShopItem) }
        )
        assertThat(newState).isEqualTo(expectedState)
    }

    @Test
    fun givenStateHasNoItems_whenReplaceListItem_thenReturnStateUnchanged() {
        val state = ListViewState(list = emptyList())
        val shopItem = ShopItem.newItem("")

        val newState = state.replaceListItem(shopItem, shopItem)

        assertThat(newState).isEqualTo(state)
    }

    @Test
    fun givenStateHasListWithItems_whenReplaceListItem_thenItemReplaced() {
        val item1 =  ShopItemBuilder.aShopItem().withName("item1").build()
        val item2 = ShopItemBuilder.aShopItem().withName("item2").build()
        val shopItems = listOf(item1, item2)
        val state = ListViewState(list = shopItems)
        val newItem2 = ShopItemBuilder.aShopItem().withName("newItem2").build()

        val newState = state.replaceListItem(item2, newItem2)

        val expectedState = ListViewState(
            list = shopItems.toMutableList().apply { set(indexOf(item2), newItem2) }
        )
        assertThat(newState).isEqualTo(expectedState)
    }

}
