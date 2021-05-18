package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeFragmentDirections
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.Companion.aShoppingList
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import com.ericthecoder.shopshopshoppinglist.util.providers.TopActivityProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class MainNavigatorImplTest {

    private val activity: AppCompatActivity = mockk()
    private val topActivityProvider: TopActivityProvider = mockk()
    private val navigator: Navigator = mockk()
    private val mainNavigator = MainNavigatorImpl(navigator, topActivityProvider)
    private val supportFragmentManager: FragmentManager = mockk()
    private val navController: NavController = mockk()
    private val navHostFragment: NavHostFragment = mockk()

    @Before
    fun setUp() {
        every { activity.supportFragmentManager } returns supportFragmentManager
        every { supportFragmentManager.findFragmentById(R.id.fragment_container_view) } returns navHostFragment
        every { navHostFragment.navController } returns navController
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
