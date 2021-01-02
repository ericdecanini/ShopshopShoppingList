package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import androidx.navigation.NavController
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class MainNavigatorImplTest {

    private val mainNavigator = MainNavigatorImpl()
    private val navController: NavController = mock()

    init {
      mainNavigator.navController = navController
    }

    @Test
    fun whenGoToList_thenNavigateToListFragment() {
        mainNavigator.goToList()

        verify(mainNavigator.navController).navigate(R.id.listFragment)
    }

    @Test
    fun givenShoppingList_whenGoToList_thenNavigateToListFragmentWithBundle() {
        val shoppingList = ShoppingListBuilder.aShoppingList().build()

        mainNavigator.goToList(shoppingList)

        verify(mainNavigator.navController).navigate(eq(R.id.listFragment), any())
    }

}
