package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MainNavigatorImplTest {


    private val activity: AppCompatActivity = mockk()
    private val mainNavigator = MainNavigatorImpl(activity)

    private val navController: NavController = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic(Navigation::class)
        every { Navigation.findNavController(any(), any()) } returns navController
    }

    @Test
    fun whenGoToList_thenNavigateToListFragment() {
        mainNavigator.goToList()

        verify { navController.navigate(R.id.listFragment) }
    }

    @Test
    fun givenShoppingList_whenGoToList_thenNavigateToListFragmentWithBundle() {
        val shoppingList = ShoppingListBuilder.aShoppingList().build()

        mainNavigator.goToList(shoppingList)

        verify { navController.navigate(eq(R.id.listFragment), any()) }
    }

}
