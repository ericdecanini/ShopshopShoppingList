package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.dialogs.DialogNavigator
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShopItemBuilder.Companion.aShopItem
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.Companion.aShoppingList
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val shoppingListRepository: ShoppingListRepository = mock()
    private val mainNavigator: MainNavigator = mock()
    private val dialogNavigator: DialogNavigator = mock()

    private val context: Context = mock()
    private val view: View = mock()
    private val imm: InputMethodManager = mock()

    private val shopItem = aShopItem().withQuantity(5).build()
    private val itemsList: MutableList<ShopItem> = mutableListOf(shopItem)
    private val shoppingList = aShoppingList().withItems(itemsList).build()

    private lateinit var viewModel: ListViewModel

    @Before
    fun setUp() {
        viewModel = ListViewModel(shoppingListRepository, mainNavigator, dialogNavigator)

        givenShoppingList()
    }

    @Test
    fun whenCreateNewShoppingList_shoppingListIsCreatedAndPosted() {
        val name = "list_name"
        given(context.getString(R.string.new_list)).willReturn(name)
        given(shoppingListRepository.createNewShoppingList(name)).willReturn(shoppingList)

        viewModel.createNewShoppingList(context)

        assertThat(viewModel.shoppingListLiveData.value).isEqualTo(shoppingList)
    }

    @Test
    fun givenRepositoryLoads_whenLoadShoppingList_thenShoppingListLoadedFromRepository() {
        val id = shoppingList.id
        given(shoppingListRepository.getShoppingListById(id)).willReturn(shoppingList)

        viewModel.loadShoppingList(id)

        assertThat(viewModel.shoppingListLiveData.value).isEqualTo(shoppingList)
        assertThat(viewModel.listName.get()).isEqualTo(shoppingList.name)
    }

    @Test
    fun givenRepositoryFailsToLoad_whenLoadShoppingList_thenNavigateUp() {
        val id = shoppingList.id
        given(shoppingListRepository.getShoppingListById(id)).willReturn(null)

        viewModel.loadShoppingList(id)

        verify(mainNavigator).navigateUp()
    }

    @Test
    fun givenItemName_whenAddItem_thenItemAddedAndAddItemTextCleared() {
        val itemName = "new_item"
        val newShopItem = aShopItem().withName(itemName).build()
        given(shoppingListRepository.createNewShopItem(shoppingList.id, itemName)).willReturn(newShopItem)

        viewModel.addItem(itemName)

        assertThat(viewModel.addItemText.get()).isEqualTo("")
        val itemsList = viewModel.shoppingListLiveData.value?.items
        assertThat(itemsList?.last()).isEqualTo(newShopItem)
        verify(shoppingListRepository).createNewShopItem(shoppingList.id, itemName)
    }

    @Test
    fun givenShopItemWithQuantityGreaterThanOne_whenOnQuantityDown_thenQuantityDecreasedAndViewUpdated() {
        val quantity = 5
        val quantityView: TextView = mock()
        shopItem.quantity = quantity

        viewModel.onQuantityDown(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity - 1)
        verify(quantityView).text = shopItem.quantity.toString()
        verify(shoppingListRepository).updateShopItem(shopItem.id, shopItem.name, quantity - 1, shopItem.checked)
    }

    @Test
    fun givenShopItemWithQuantityOne_whenOnQuantityDown_thenQuantityStaysTheSame() {
        val quantity = 1
        val quantityView: TextView = mock()
        shopItem.quantity = quantity

        viewModel.onQuantityDown(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity)
        verifyZeroInteractions(quantityView)
        verify(shoppingListRepository, never()).updateShopItem(any(), any(), any(), any())
    }

    @Test
    fun givenShopItem_whenOnQuantityUp_thenQuantityIncreasedAndViewUpdated() {
        val quantity = 5
        val quantityView: TextView = mock()
        shopItem.quantity = quantity

        viewModel.onQuantityUp(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity + 1)
        verify(quantityView).text = shopItem.quantity.toString()
        verify(shoppingListRepository).updateShopItem(shopItem.id, shopItem.name, quantity + 1, shopItem.checked)
    }

    @Test
    fun givenShopItem_whenOnDeleteClick_thenItemDeletedFromList() {

        viewModel.onDeleteClick(shopItem)

        val deletedItem = viewModel.shoppingListLiveData.value?.items?.find { it == shopItem }
        assertThat(deletedItem).isNull()
        verify(shoppingListRepository).deleteShopItem(shopItem.id)
    }

    @Test
    fun givenCheckboxIsChecked_whenOnCheckboxChanged_themShopItemIsChecked() {
        val checkBox: CheckBox = mock()
        given(checkBox.isChecked).willReturn(true)

        viewModel.onCheckboxChecked(checkBox, shopItem)

        assertThat(shopItem.checked).isTrue
        verify(shoppingListRepository).updateShopItem(shopItem.id, shopItem.name, shopItem.quantity, true)
    }

    @Test
    fun givenCheckboxIsNotChecked_whenOnCheckboxChanged_themShopItemIsNotChecked() {
        val checkBox: CheckBox = mock()
        given(checkBox.isChecked).willReturn(false)

        viewModel.onCheckboxChecked(checkBox, shopItem)

        assertThat(shopItem.checked).isFalse
        verify(shoppingListRepository).updateShopItem(shopItem.id, shopItem.name, shopItem.quantity, false)
    }

    @Test
    fun givenEditText_whenOnNameChanged_thenNameChangedToEditTextValueAndKeyboardHidden() {
        val editText: EditText = mock()
        val editable: Editable = mock()
        val name = "sample_name"
        given(editable.toString()).willReturn(name)
        given(editText.text).willReturn(editable)
        given(editText.context).willReturn(context)
        given(context.getSystemService(Activity.INPUT_METHOD_SERVICE)).willReturn(imm)

        viewModel.onNameChanged(editText, shopItem)

        assertThat(shopItem.name).isEqualTo(name)
        verify(imm).hideSoftInputFromWindow(eq(editText.windowToken), any())
        verify(shoppingListRepository).updateShopItem(shopItem.id, name, shopItem.quantity, shopItem.checked)
    }

    @Test
    fun givenView_whenHideKeyboard_thenKeyboardHidden() {
        given(view.context).willReturn(context)
        given(context.getSystemService(Activity.INPUT_METHOD_SERVICE)).willReturn(imm)

        viewModel.hideKeyboard(view)

        verify(imm).hideSoftInputFromWindow(eq(view.windowToken), any())
    }

    @Test
    fun givenNewName_whenRenameDialogCallback_thenRenameShoppingList() {
        val currentName = viewModel.shoppingListLiveData.value!!.name
        val newName = "new_name"

        viewModel.showRenameDialog()

        val callbackCaptor = argumentCaptor<(String) -> Unit>()
        verify(dialogNavigator).displayRenameDialog(eq(currentName), callbackCaptor.capture(), eq(null), eq(true))
        callbackCaptor.firstValue.invoke(newName)

        assertThat(viewModel.listName.get()).isEqualTo(newName)
        verify(shoppingListRepository).updateShoppingList(any(), eq(newName))
    }

    private fun givenShoppingList() {
        given(shoppingListRepository.getShoppingListById(shoppingList.id)).willReturn(shoppingList)
        viewModel.loadShoppingList(shoppingList.id)
    }
}
