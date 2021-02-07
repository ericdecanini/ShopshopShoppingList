package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home.HomeFragmentDirections
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.Companion.aShoppingList
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

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
    fun givenNoShoppingList_whenGoToList_thenNavigateToListFragmentWithNoShoppingListId() {
        mainNavigator.goToList()

        val slot = slot<HomeFragmentDirections.ActionHomeFragmentToListFragment>()
        verify { navController.navigate(capture(slot)) }
        assertThrows<NullPointerException> { slot.captured.shoppingListId }
    }

    @Test
    fun givenShoppingList_whenGoToList_thenNavigateToListFragmentWithListId() {
        val listId = 5
        val shoppingList = aShoppingList().withId(listId).build()

        mainNavigator.goToList(shoppingList)

        val slot = slot<HomeFragmentDirections.ActionHomeFragmentToListFragment>()
        verify { navController.navigate(capture(slot)) }
        assertThat(slot.captured.shoppingListId).isEqualTo(listId)
    }

    @Test
    fun whenNavigateUp_thenNavigateUpWithController() {

        mainNavigator.navigateUp()

        verify { navController.navigateUp() }
    }

}
