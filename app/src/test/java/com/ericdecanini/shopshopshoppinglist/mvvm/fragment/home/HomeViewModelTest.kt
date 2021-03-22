package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.util.CoroutineContextProvider
import com.ericdecanini.shopshopshoppinglist.util.TestCoroutineContextProvider
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mainNavigator: MainNavigator = mock()
    private val shoppingListRepository: ShoppingListRepository = mock()
    private val coroutineContextProvider: CoroutineContextProvider = TestCoroutineContextProvider()
    private val viewModel = HomeViewModel(mainNavigator, shoppingListRepository, coroutineContextProvider)

    private val shoppingList = ShoppingListBuilder.aShoppingList().build()

    @Test
    fun givenRepositoryReturnsShoppingLists_whenRefreshLists_thenPostShoppingListsToLiveData() = runBlockingTest {
        val shoppingLists = listOf(shoppingList)
        given(shoppingListRepository.getShoppingLists()).willReturn(shoppingLists)

        viewModel.refreshLists()

        assertThat(viewModel.stateLiveData.value).isEqualTo(HomeViewState.Loaded(shoppingLists))
    }

    @Test
    fun givenRepositoryReturnsNoShoppingLists_whenRefreshLists_thenPostEmptyListToLiveData() = runBlockingTest {
        given(shoppingListRepository.getShoppingLists()).willReturn(null)

        viewModel.refreshLists()

        assertThat(viewModel.stateLiveData.value).isEqualTo(HomeViewState.Loaded(emptyList()))
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
