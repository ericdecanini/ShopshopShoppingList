package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShopItemBuilder.Companion.aShopItem
import com.ericdecanini.shopshopshoppinglist.usecases.viewstate.ListViewState
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewStateProvider: ViewStateProvider = mock()
    private val viewState: ListViewState = mock()
    private val mockList: MutableList<ShopItem> = mock()

    private val shopItem = aShopItem().withQuantity(5).build()

    private lateinit var viewModel: ListViewModel

    @Before
    fun setUp() {
        given(viewStateProvider.create(ListViewState::class.java)).willReturn(viewState)
        given(viewState.list).willReturn(mockList)

        viewModel = ListViewModel(viewStateProvider)
    }

    @Test
    fun givenItemName_whenAddItem_thenItemAddedAndAddItemTextCleared() {
        val itemName = "new_item"

        viewModel.addItem(itemName)

        assertThat(viewModel.addItemText.get()).isEqualTo("")
        val newItemCaptor = argumentCaptor<ShopItem>()
        verify(mockList).add(newItemCaptor.capture())
        assertThat(newItemCaptor.firstValue.name).isEqualTo(itemName)
    }

    @Test
    fun givenShopItem_whenOnQuantityDown_thenItemReplacedWithMinusOneQuantity() {
        val itemIndex = 1
        given(mockList.indexOf(shopItem)).willReturn(itemIndex)

        viewModel.onQuantityDown(shopItem)

        val itemCaptor = argumentCaptor<ShopItem>()
        verify(mockList)[eq(itemIndex)] = itemCaptor.capture()
        assertThat(itemCaptor.firstValue).isEqualTo(shopItem.copy(quantity = shopItem.quantity - 1))
    }

    @Test
    fun givenShopItem_whenOnQuantityUp_thenItemReplacedWithPlusOneQuantity() {
        val itemIndex = 1
        given(mockList.indexOf(shopItem)).willReturn(itemIndex)

        viewModel.onQuantityUp(shopItem)

        val itemCaptor = argumentCaptor<ShopItem>()
        verify(mockList)[eq(itemIndex)] = itemCaptor.capture()
        assertThat(itemCaptor.firstValue).isEqualTo(shopItem.copy(quantity = shopItem.quantity + 1))
    }

    @Test
    fun givenShopItem_whenOnDeleteClick_thenItemDeletedFromList() {

        viewModel.onDeleteClick(shopItem)

        verify(mockList).remove(shopItem)
    }

    @Test
    fun givenCheckboxIsChecked_whenOnCheckboxChanged_themShopItemIsChecked() {
        val checkBox: CheckBox = mock()
        given(checkBox.isChecked).willReturn(true)

        viewModel.onCheckboxChecked(checkBox, shopItem)

        assertThat(shopItem.checked).isTrue
    }

    @Test
    fun givenCheckboxIsNotChecked_whenOnCheckboxChanged_themShopItemIsNotChecked() {
        val checkBox: CheckBox = mock()
        given(checkBox.isChecked).willReturn(false)

        viewModel.onCheckboxChecked(checkBox, shopItem)

        assertThat(shopItem.checked).isFalse
    }

    @Test
    fun givenEditText_whenOnNameChanged_thenNameChangedToEditTextValue() {
        val editText: EditText = mock()
        val editable: Editable = mock()
        val name = "sample_name"
        given(editable.toString()).willReturn(name)
        given(editText.text).willReturn(editable)

        viewModel.onNameChanged(editText, shopItem)

        assertThat(shopItem.name).isEqualTo(name)
    }

    @Test
    fun givenView_whenHideKeyboard_thenKeyboardHidden() {
        val context: Context = mock()
        val view: View = mock()
        val imm: InputMethodManager = mock()
        given(view.context).willReturn(context)
        given(context.getSystemService(Activity.INPUT_METHOD_SERVICE)).willReturn(imm)

        viewModel.hideKeyboard(view)

        verify(imm).hideSoftInputFromWindow(eq(view.windowToken), any())
    }

}
