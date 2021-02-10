package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mainNavigator: MainNavigator = mock()
    private val shoppingListRepository: ShoppingListRepository = mock()
    private lateinit var viewModel: HomeViewModel
    private val shoppingList = ShoppingListBuilder.aShoppingList().build()

    @Before
    fun setUp() {
        viewModel = HomeViewModel(mainNavigator, shoppingListRepository)
    }

    @Test
    fun givenRepositoryReturnsShoppingLists_whenViewModelInit_thenSetShoppingListsToLiveData() {
        val shoppingLists = listOf(shoppingList)
        given(shoppingListRepository.getShoppingLists()).willReturn(shoppingLists)

        viewModel = HomeViewModel(mainNavigator, shoppingListRepository)

        assertThat(viewModel.shoppingListsLiveData.value).isEqualTo(shoppingLists)
    }

    @Test
    fun givenRepositoryReturnsNoShoppingLists_whenViewModelInit_thenSetEmptyListToLiveData() {
        given(shoppingListRepository.getShoppingLists()).willReturn(null)

        viewModel = HomeViewModel(mainNavigator, shoppingListRepository)

        assertThat(viewModel.shoppingListsLiveData.value).isEqualTo(emptyList<ShoppingList>())
    }

    @Test
    fun givenShoppingList_whenOnShoppingListClick_thenMainNavigatorWithShoppingList() {

        viewModel.onShoppingListClick(shoppingList)

        verify(mainNavigator).goToList(shoppingList)
    }

    @Test
    fun whenNavigateToListFragment_thenMainNavigatorGoToList() {

        viewModel.navigateToListFragment()

        verify(mainNavigator).goToList()
    }
}
