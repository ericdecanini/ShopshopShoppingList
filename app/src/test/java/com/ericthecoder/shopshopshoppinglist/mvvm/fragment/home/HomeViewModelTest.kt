package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.aShoppingList
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.util.InstantTaskExecutorExtension
import com.ericthecoder.shopshopshoppinglist.util.TestCoroutineContextProvider
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class HomeViewModelTest {

    private val shoppingListRepository: ShoppingListRepository = mockk()
    private val coroutineContextProvider: CoroutineContextProvider = TestCoroutineContextProvider()
    private val persistentStorageReader: PersistentStorageReader = mockk()

    private val viewModel = HomeViewModel(
        shoppingListRepository,
        coroutineContextProvider,
        persistentStorageReader,
    )

    private val shoppingList = aShoppingList()

    @Nested
    inner class RefreshLists {

        @Test
        fun `refreshLists posts loaded state with lists`() = runBlockingTest {
            val shoppingLists = listOf(shoppingList)
            coEvery { shoppingListRepository.getShoppingLists() } returns shoppingLists

            viewModel.refreshLists()

            assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Loaded(shoppingLists))
        }

        @Test
        fun `when no lists are found, refreshLists posts loaded state with empty list`() = runBlockingTest {
            coEvery { shoppingListRepository.getShoppingLists() } returns null

            viewModel.refreshLists()

            assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Loaded(emptyList()))
        }

        @Test
        fun `when error is thrown, refreshLists posts error state`() = runBlockingTest {
            val exception = RuntimeException()
            coEvery { shoppingListRepository.getShoppingLists() } throws exception

            viewModel.refreshLists()

            assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Error(exception))
        }
    }

    @Nested
    inner class Events {

        @Test
        fun `onShoppingListClick opens clicked list`() {

            viewModel.onShoppingListClick(shoppingList)

            assertThat(viewModel.viewEvent.value).isEqualTo(OpenList(shoppingList))
        }

        @Test
        fun `navigateToListFragment opens list screen with no arguments`() {

            viewModel.navigateToListFragment()

            assertThat(viewModel.viewEvent.value).isEqualTo(OpenList(null))
        }

        @Test
        fun `navigateToUpsell opens upsell page`() {

            viewModel.navigateToUpsell()

            assertThat(viewModel.viewEvent.value).isEqualTo(OpenUpsell)
        }

        @Test
        fun `onToolbarClick cycles theme`() {

            viewModel.onToolbarClick()

            assertThat(viewModel.viewEvent.value).isEqualTo(CycleTheme)
        }
    }
}
