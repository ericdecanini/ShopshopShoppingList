package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.aShoppingList
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import com.ericthecoder.shopshopshoppinglist.util.InstantTaskExecutorExtension
import com.ericthecoder.shopshopshoppinglist.util.TestCoroutineContextProvider
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
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

    @BeforeEach
    fun setup() {
        every { persistentStorageReader.hasChangedTheme() } returns true
    }

    @Nested
    inner class RefreshLists {

        @Test
        fun `refreshLists posts loaded state with lists`() = runBlockingTest {
            val shoppingLists = listOf(shoppingList)
            coEvery { shoppingListRepository.getShoppingLists() } returns shoppingLists
            val viewModel = initViewModel()

            viewModel.refreshLists()

            assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Loaded(shoppingLists))
        }

        @Test
        fun `when no lists are found, refreshLists posts loaded state with empty list`() = runBlockingTest {
            coEvery { shoppingListRepository.getShoppingLists() } returns null
            val viewModel = initViewModel()

            viewModel.refreshLists()

            assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Loaded(emptyList()))
        }

        @Test
        fun `when error is thrown, refreshLists posts error state`() = runBlockingTest {
            val exception = RuntimeException()
            coEvery { shoppingListRepository.getShoppingLists() } throws exception
            val viewModel = initViewModel()

            viewModel.refreshLists()

            assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Error(exception))
        }
    }

    @Nested
    inner class TitleBreathing {

        @Test
        fun `given theme has not changed, set title breathing on init`() {
            every { persistentStorageReader.hasChangedTheme() } returns false

            val viewModel = initViewModel()

            assertThat(viewModel.viewEvent.value).isEqualTo(SetPlayingBreatheTitle(true))
        }

        @Test
        fun `when breathing title is clicked, set title breathing to false and save`() {
            every { persistentStorageReader.hasChangedTheme() } returns false

            val viewModel = initViewModel()
            viewModel.onToolbarClick()

            assertThat(viewModel.viewEvent.value).isEqualTo(SetPlayingBreatheTitle(false))
            verify { persistentStorageWriter.setHasChangedTheme(true) }
        }

        @Test
        fun `when non-breathing title is clicked, do not perform saving to prefs`() {
            every { persistentStorageReader.hasChangedTheme() } returns true

            val viewModel = initViewModel()
            viewModel.onToolbarClick()

            verify { persistentStorageWriter wasNot called }
        }
    }

    @Nested
    inner class Events {

        @Test
        fun `onShoppingListClick opens clicked list`() {
            val viewModel = initViewModel()

            viewModel.onShoppingListClick(shoppingList)

            assertThat(viewModel.viewEvent.value).isEqualTo(OpenList(shoppingList))
        }

        @Test
        fun `navigateToListFragment opens list screen with no arguments`() {
            val viewModel = initViewModel()

            viewModel.navigateToListFragment()

            assertThat(viewModel.viewEvent.value).isEqualTo(OpenList(null))
        }

        @Test
        fun `navigateToUpsell opens upsell page`() {
            val viewModel = initViewModel()

            viewModel.navigateToUpsell()

            assertThat(viewModel.viewEvent.value).isEqualTo(OpenUpsell)
        }

        @Test
        fun `onToolbarClick cycles theme`() {
            val viewModel = initViewModel()

            viewModel.onToolbarClick()

            assertThat(viewModel.viewEvent.value).isEqualTo(CycleTheme)
        }
    }

    private fun initViewModel() = HomeViewModel(
        shoppingListRepository,
        coroutineContextProvider,
        persistentStorageReader,
        persistentStorageWriter,
    )
}
