package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericdecanini.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.mvvm.viewstate.ListViewState
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProviderImpl
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewStateProvider: ViewStateProviderImpl = mock()
    private val viewState: ListViewState = mock()

    private val viewModel = ListViewModel(viewStateProvider)

    private val sampleShopItem = ShopItem.newItem("name")

    @Before
    fun setUp() {
        given(viewStateProvider.create<ListViewState>(any())).willReturn(viewState)
    }

    @Test
    fun givenReplaceItemReturnsShopItem_whenOnItemUpdate_thenStateLiveDataIsUpdated() {
        val outputViewState = viewState.copy(list = listOf(sampleShopItem))
        given(viewState.replaceItem(any(), any())).willReturn(outputViewState)

        viewModel.onItemUpdate.invoke(sampleShopItem, sampleShopItem)

        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenAddItemReturns_whenOnAddItemClick_thenStateLiveDataIsUpdated() {
        val outputViewState = viewState.copy(list = listOf(sampleShopItem))
        given(viewState.addItem(any())).willReturn(outputViewState)

        viewModel.onAddItemClick("")

        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenDeleteItemReturns_whenOnItemDelete_thenStateLiveDataIsUpdated() {
        val outputViewState = viewState.copy(list = listOf(sampleShopItem))
        given(viewState.deleteItem(any())).willReturn(outputViewState)

        viewModel.onItemDelete.invoke(sampleShopItem)

        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }
}
