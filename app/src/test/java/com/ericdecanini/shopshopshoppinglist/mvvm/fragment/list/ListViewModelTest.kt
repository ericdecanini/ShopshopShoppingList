package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShopItemBuilder.Companion.aShopItem
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.Companion.aShoppingList
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.nhaarman.mockitokotlin2.any
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

    private val shoppingListRepository: ShoppingListRepository = mock()
    private val mainNavigator: MainNavigator = mock()

    private val context: Context = mock()
    private val view: View = mock()
    private val imm: InputMethodManager = mock()

    private val shopItem = aShopItem().withQuantity(5).build()
    private val itemsList: MutableList<ShopItem> = mutableListOf(shopItem)
    private val shoppingList = aShoppingList().withItems(itemsList).build()

    private lateinit var viewModel: ListViewModel

    @Before
    fun setUp() {
        viewModel = ListViewModel(shoppingListRepository, mainNavigator)

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
        given(shoppingListRepository.createNewShopItem(any(), eq(itemName))).willReturn(newShopItem)

        viewModel.addItem(itemName)

        assertThat(viewModel.addItemText.get()).isEqualTo("")
        val itemsList = viewModel.shoppingListLiveData.value?.items
        assertThat(itemsList?.last()).isEqualTo(newShopItem)
    }

    @Test
    fun givenShopItem_whenOnQuantityDown_thenItemReplacedWithMinusOneQuantity() {
        val itemIndex = shoppingList.items.indexOf(shopItem)

        viewModel.onQuantityDown(shopItem)

        val newShopItem = viewModel.shoppingListLiveData.value?.items
        assertThat(newShopItem?.get(itemIndex)?.quantity).isEqualTo(shopItem.quantity - 1)
    }

    @Test
    fun givenShopItem_whenOnQuantityUp_thenItemReplacedWithPlusOneQuantity() {
        val itemIndex = shoppingList.items.indexOf(shopItem)

        viewModel.onQuantityUp(shopItem)

        val newShopItem = viewModel.shoppingListLiveData.value?.items
        assertThat(newShopItem?.get(itemIndex)?.quantity).isEqualTo(shopItem.quantity + 1)
    }

    @Test
    fun givenShopItem_whenOnDeleteClick_thenItemDeletedFromList() {

        viewModel.onDeleteClick(shopItem)

        val shopItem = viewModel.shoppingListLiveData.value?.items?.find { it == shopItem }
        assertThat(shopItem).isNull()
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
    }

    @Test
    fun givenView_whenHideKeyboard_thenKeyboardHidden() {
        given(view.context).willReturn(context)
        given(context.getSystemService(Activity.INPUT_METHOD_SERVICE)).willReturn(imm)

        viewModel.hideKeyboard(view)

        verify(imm).hideSoftInputFromWindow(eq(view.windowToken), any())
    }

    private fun givenShoppingList() {
        val id = 1
        given(shoppingListRepository.getShoppingListById(id)).willReturn(shoppingList)
        viewModel.loadShoppingList(id)
    }
}
