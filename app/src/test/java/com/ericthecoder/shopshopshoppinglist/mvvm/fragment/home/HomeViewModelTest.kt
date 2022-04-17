package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.aShoppingList
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import com.ericthecoder.shopshopshoppinglist.util.InstantTaskExecutorExtension
import com.ericthecoder.shopshopshoppinglist.util.TestCoroutineContextProvider
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    private val resourceProvider: ResourceProvider = mockk()

    private val viewModel = HomeViewModel(
        shoppingListRepository,
        coroutineContextProvider,
        persistentStorageReader,
        persistentStorageWriter,
        resourceProvider,
    )

    private val shoppingList = aShoppingList()

    @Nested
    inner class Theming {

        private val themeColors = intArrayOf(0xFFFF0, 0xFFFF1, 0xFFFF2)

        @BeforeEach
        fun setup() {
            every { resourceProvider.getColorArray(R.array.theme_colors) } returns themeColors
        }

        @Test
        fun `initTheme sets theme color`() {
            val themeColorIndex = 1
            every { persistentStorageReader.getCurrentThemeColorIndex() } returns themeColorIndex

            viewModel.initTheme()

            assertThat(viewModel.viewEvent.value).isEqualTo(SetThemeColor(themeColors[themeColorIndex]))
        }

        @Test
        fun `onToolbarClick cycles to the next theme`() {
            val themeColorIndex = 1
            every { persistentStorageReader.getCurrentThemeColorIndex() } returns themeColorIndex

            viewModel.onToolbarClick()

            assertThat(viewModel.viewEvent.value).isEqualTo(SetThemeColor(themeColors[2]))
            verify { persistentStorageWriter.setCurrentThemeColorIndex(2) }
        }

        @Test
        fun `when current theme color is last in set, onToolbarClick cycles back to the first theme`() {
            val themeColorIndex = 2
            every { persistentStorageReader.getCurrentThemeColorIndex() } returns themeColorIndex

            viewModel.onToolbarClick()

            assertThat(viewModel.viewEvent.value).isEqualTo(SetThemeColor(themeColors[0]))
            verify { persistentStorageWriter.setCurrentThemeColorIndex(0) }
        }
    }

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
    }
}
