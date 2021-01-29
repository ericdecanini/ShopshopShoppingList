package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder
import com.ericdecanini.shopshopshoppinglist.usecases.viewstate.HomeViewState
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class HomeViewStateTest {

    private val state = HomeViewState(listOf())

    @Test
    fun givenListOfShoppingList_whenWithShoppingLists_thenViewStateCreatedWithShoppingLists() {
        val lists = listOf(ShoppingListBuilder.aShoppingList().build())

        val newState = state.withShoppingLists(lists)

        assertThat(newState).isEqualTo(state.copy(shoppingLists = lists))
    }
}
