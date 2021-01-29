package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.text.Editable
import android.widget.CheckBox
import android.widget.EditText
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.usecases.viewstate.ListViewState
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewStateProvider: ViewStateProvider = mock()
    private val viewState: ListViewState = mock()
    private val editText: EditText = mock()
    private val editable: Editable = mock()
    private val checkbox: CheckBox = mock()

    private lateinit var viewModel: ListViewModel

    private val sampleShopItem = ShopItem.newItem("name")
    private val outputViewState = viewState.copy(list = listOf(sampleShopItem))

    @Before
    fun setUp() {
        given(viewStateProvider.create(ListViewState::class.java)).willReturn(viewState)
        viewModel = ListViewModel(viewStateProvider)
    }

    @Test
    fun givenAddItemReturns_whenAddItem_thenLiveDataIsUpdated() {
        given(viewState.addItem(any())).willReturn(outputViewState)

        viewModel.addItem(sampleShopItem.name)

        verify(viewState, times(1)).addItem(ShopItem.newItem(sampleShopItem.name))
        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenReplaceItemsReturnsAndCurrentQuantityIsOne_whenOnQuantityDown_thenLiveDataIsUpdated() {
        val sampleShopItem = sampleShopItem.copy(quantity = 1)
        given(viewState.replaceItem(any(), any())).willReturn(outputViewState)

        viewModel.onQuantityDown(sampleShopItem)

        verify(viewState, times(1)).replaceItem(sampleShopItem, sampleShopItem)
        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenReplaceItemsReturnsAndCurrentQuantityIsGreaterThanOne_whenOnQuantityDown_thenLiveDataIsUpdated() {
        val sampleShopItem = sampleShopItem.copy(quantity = 2)
        given(viewState.replaceItem(any(), any())).willReturn(outputViewState)

        viewModel.onQuantityDown(sampleShopItem)

        verify(viewState, times(1)).replaceItem(sampleShopItem, sampleShopItem.copy(quantity = sampleShopItem.quantity - 1))
        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenReplaceItemsReturns_whenOnQuantityUp_thenLiveDataIsUpdated() {
        given(viewState.replaceItem(any(), any())).willReturn(outputViewState)

        viewModel.onQuantityUp(sampleShopItem)

        verify(viewState, times(1)).replaceItem(sampleShopItem, sampleShopItem.copy(quantity = sampleShopItem.quantity + 1))
        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenDeleteItemReturns_whenOnDeleteClick_thenLiveDataIsUpdated() {
        given(viewState.deleteItem(any())).willReturn(outputViewState)

        viewModel.onDeleteClick(sampleShopItem)

        verify(viewState, times(1)).deleteItem(sampleShopItem)
        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenReplaceItemsReturnsAndCheckboxIsChecked_whenOnCheckboxChecked_thenLiveDataIsUpdated() {
        val checked = true
        given(viewState.replaceItem(any(), any())).willReturn(outputViewState)
        given(checkbox.isChecked).willReturn(checked)

        viewModel.onCheckboxChecked(checkbox, sampleShopItem)

        verify(viewState, times(1)).replaceItem(sampleShopItem, sampleShopItem.copy(checked = checked))
        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenReplaceItemsReturnsAndCheckboxIsNotChecked_whenOnCheckboxChecked_thenLiveDataIsUpdated() {
        val checked = false
        given(viewState.replaceItem(any(), any())).willReturn(outputViewState)
        given(checkbox.isChecked).willReturn(checked)

        viewModel.onCheckboxChecked(checkbox, sampleShopItem)

        verify(viewState, times(1)).replaceItem(sampleShopItem, sampleShopItem.copy(checked = checked))
        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenViewIsNotCheckbox_whenOnCheckboxChecked_thenThrowClassCastException() {
        assertThrows<ClassCastException> { viewModel.onCheckboxChecked(editText, sampleShopItem) }
    }

    @Test
    fun givenReplaceItemReturns_whenOnNameChanged_thenLiveDataIsUpdated() {
        val newName = "newname"
        given(viewState.replaceItem(any(), any())).willReturn(outputViewState)
        given(editText.text).willReturn(editable)
        given(editable.toString()).willReturn(newName)

        viewModel.onNameChanged(editText, sampleShopItem)

        verify(viewState, times(1)).replaceItem(sampleShopItem, sampleShopItem.withName(newName))
        assertThat(viewModel.stateLiveData.value).isEqualTo(outputViewState)
    }

    @Test
    fun givenViewIsNotEditText_whenOnNameChanged_thenThrowClassCastException() {
        assertThrows<ClassCastException> { viewModel.onNameChanged(checkbox, sampleShopItem) }
    }
}
