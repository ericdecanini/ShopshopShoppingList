package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home.HomeFragmentDirections
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.Companion.aShoppingList
import com.ericdecanini.shopshopshoppinglist.util.navigator.Navigator
import com.ericdecanini.shopshopshoppinglist.util.providers.TopActivityProvider
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class MainNavigatorImplTest {

    private val activity: AppCompatActivity = mockk()
    private val topActivityProvider: TopActivityProvider = mockk()
    private val navigator: Navigator = mockk()
    private val mainNavigator = MainNavigatorImpl(navigator, topActivityProvider)

    private val navController: NavController = mockk()

    @Before
    fun setUp() {
        mockkStatic(Navigation::class)
        every { Navigation.findNavController(any(), any()) } returns navController
        every { navController.navigate(any<NavDirections>()) } returns Unit
        every { navController.navigateUp() } returns true
        every { topActivityProvider.getTopActivity() } returns activity
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
