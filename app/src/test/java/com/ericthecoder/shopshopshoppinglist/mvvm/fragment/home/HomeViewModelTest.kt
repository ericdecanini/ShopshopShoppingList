package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import com.ericthecoder.shopshopshoppinglist.entities.theme.Theme
import com.ericthecoder.shopshopshoppinglist.library.extension.observeWithMock
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.aShoppingList
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import com.ericthecoder.shopshopshoppinglist.util.InstantTaskExecutorExtension
import com.ericthecoder.shopshopshoppinglist.util.TestCoroutineContextProvider
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
    private val persistentStorageWriter: PersistentStorageWriter = mockk(relaxUnitFun = true)

    private val shoppingList = aShoppingList()

    private val viewModel = HomeViewModel(
        shoppingListRepository,
        coroutineContextProvider,
        persistentStorageReader,
        persistentStorageWriter,
    )

    @Nested
    inner class RefreshLists {

        @Test
        fun `refreshLists posts loaded state with lists`() = runTest {
            val shoppingLists = listOf(shoppingList)
            coEvery { shoppingListRepository.getShoppingLists() } returns shoppingLists

            viewModel.refreshLists()

            assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Loaded(shoppingLists, false))
        }

        @Test
        fun `when no lists are found, refreshLists posts loaded state with empty list`() = runTest {
            coEvery { shoppingListRepository.getShoppingLists() } returns null

            viewModel.refreshLists()

            assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Loaded(emptyList(), false))
        }

        @Test
        fun `when error is thrown, refreshLists posts error state`() = runTest {
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
        fun `handleUpgradeMenuItem opens upsell page`() {

            viewModel.handleUpgradeMenuItem()

            assertThat(viewModel.viewEvent.value).isEqualTo(OpenUpsell)
        }

        @Test
        fun `handleSettingsMenuItem opens settings page`() {

            viewModel.handleSettingsMenuItem()

            assertThat(viewModel.viewEvent.value).isEqualTo(OpenSettings)
        }

        @Test
        fun `switchToTheme saves theme and recreates activity`() {
            val observer = viewModel.viewEvent.observeWithMock()
            val theme = Theme.GREEN

            viewModel.switchToTheme(theme)

            verifyOrder {
                persistentStorageWriter.setCurrentTheme(theme.name)
                observer.onChanged(RecreateActivity)
            }
        }
    }
}
