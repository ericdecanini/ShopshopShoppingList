package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import com.ericdecanini.testdata.testdatabuilders.ShoppingListBuilder
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewState: HomeViewState = mock()
    private val mainNavigator: MainNavigator = mock()
    private val viewStateProvider: ViewStateProvider = mock()
    private lateinit var viewModel: HomeViewModel

    private val navController: NavController = mock()

    @Before
    fun setUp() {
        given(viewStateProvider.create<HomeViewState>(any())).willReturn(viewState)
        viewModel = HomeViewModel(mainNavigator, viewStateProvider)
        viewModel.setControllerForNavigator(navController)
    }

    @Test
    fun givenShoppingList_whenOnShoppingListClick_thenMainNavigatorWithShoppingList() {
        val shoppingList = ShoppingListBuilder.aShoppingList().build()

        viewModel.onShoppingListClick.invoke(shoppingList)

        verify(mainNavigator).goToList(shoppingList)
    }

    @Test
    fun whenNavigateToListFragment_thenMainNavigatorGoToList() {
        viewModel.navigateToListFragment()

        verify(mainNavigator).goToList()
    }

}
