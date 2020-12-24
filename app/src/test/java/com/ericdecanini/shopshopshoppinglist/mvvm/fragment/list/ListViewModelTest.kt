package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericdecanini.entities.ShopItem
import com.ericdecanini.entities.ViewState
import com.ericdecanini.shopshopshoppinglist.mvvm.viewstate.ListViewState
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import com.ericdecanini.testdata.testdatabuilders.ShopItemBuilder
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewStateProvider: ViewStateProvider = mock()
    private val viewState: ListViewState = mock()

    private val viewModel = ListViewModel(viewStateProvider)

    private val sampleShopItem = ShopItem.newItem("name")

    @Before
    fun setUp() {
        given(viewStateProvider.create<ListViewState>(any())).willReturn(viewState)
    }

    @Test
    fun givenReplaceListItemReturnsShopItem_whenOnItemNameChanged_thenStateLiveDataIsUpdated() {
        val outputViewState = viewState.copy(list = listOf(sampleShopItem))
        given(viewState.replaceListItem(any(), any())).willReturn(outputViewState)

        viewModel.onItemNameChanged.invoke(sampleShopItem, sampleShopItem)

        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenAddNewItemReturns_whenOnAddItemClick_thenStateLiveDataIsUpdated() {
        val outputViewState = viewState.copy(list = listOf(sampleShopItem))
        given(viewState.addNewItem(any())).willReturn(outputViewState)

        viewModel.onAddItemClick("")

        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }
}
