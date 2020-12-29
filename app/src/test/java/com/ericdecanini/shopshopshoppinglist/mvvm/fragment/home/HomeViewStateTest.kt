package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import com.ericdecanini.entities.ShopItem
import com.ericdecanini.entities.ShoppingList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class HomeViewStateTest {

    private val state = HomeViewState(listOf())

    @Test
    fun givenListOfShoppingList_whenWithLists_thenViewStateCreatedWithLists() {
        val lists = generateDummyLists()

        val newState = state.withLists(lists)

        assertThat(newState).isEqualTo(state.copy(lists = lists))
    }

    companion object {
        private fun generateDummyLists(): List<ShoppingList> = listOf(
            ShoppingList("Shoplist One", listOf(
                ShopItem.newItem("ItemA1"), ShopItem.newItem("ItemA2"), ShopItem.newItem("ItemA3"), ShopItem.newItem("ItemA4"), ShopItem.newItem("ItemA5")
            )),
            ShoppingList("Shoplist Two", listOf(
                ShopItem.newItem("ItemB1")
            )),
            ShoppingList("Shoplist Three", listOf(
                ShopItem.newItem("ItemC1"), ShopItem.newItem("ItemC2")
            ))
        )
    }

}
