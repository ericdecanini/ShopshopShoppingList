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
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.ericdecanini.dependencies.android.resources.ResourceProvider
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list.ListViewState.Error
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list.ListViewState.Loaded
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShopItemBuilder.Companion.aShopItem
import com.ericdecanini.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.Companion.aShoppingList
import com.ericdecanini.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericdecanini.shopshopshoppinglist.ui.toast.ToastNavigator
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.util.providers.CoroutineContextProvider
import com.ericdecanini.shopshopshoppinglist.util.TestCoroutineContextProvider
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val shoppingListRepository: ShoppingListRepository = mock()
    private val mainNavigator: MainNavigator = mock()
    private val dialogNavigator: DialogNavigator = mock()
    private val resourceProvider: ResourceProvider = mock()
    private val coroutineContextProvider: CoroutineContextProvider = TestCoroutineContextProvider()
    private val toastNavigator: ToastNavigator = mock()

    private val context: Context = mock()
    private val view: View = mock()
    private val imm: InputMethodManager = mock()

    private val shopItem = aShopItem().withQuantity(5).build()
    private val itemsList: MutableList<ShopItem> = mutableListOf(shopItem)
    private val shoppingList = aShoppingList().withItems(itemsList).build()

    private lateinit var viewModel: ListViewModel

    @Before
    fun setUp() {
        viewModel = ListViewModel(
            shoppingListRepository,
            mainNavigator,
            dialogNavigator,
            resourceProvider,
            coroutineContextProvider,
            toastNavigator
        )
    }

    @Test
    fun givenRepositoryLoads_whenCreateNewShoppingList_thenShoppingListIsCreatedAndPosted() = runBlockingTest {
        val name = "list_name"
        given(resourceProvider.getString(R.string.new_list)).willReturn(name)
        given(shoppingListRepository.createNewShoppingList(name)).willReturn(shoppingList)

        viewModel.createNewShoppingList()

        assertThat(viewModel.stateLiveData.value).isEqualTo(Loaded(shoppingList))
        assertThat(viewModel.listName.get()).isEqualTo(shoppingList.name)
    }

    @Test
    fun givenRepositoryThrows_whenCreateNewShoppingList_thenPostErrorToLiveData() = runBlockingTest {
        val exception = RuntimeException()
        given(resourceProvider.getString(R.string.new_list)).willReturn("")
        given(shoppingListRepository.createNewShoppingList(any())).willThrow(exception)

        viewModel.createNewShoppingList()

        assertThat(viewModel.stateLiveData.value).isEqualTo(Error(exception))
        assertThat(viewModel.listName.get()).isNull()
    }

    @Test
    fun givenRepositoryLoads_whenLoadShoppingList_thenShoppingListLoadedFromRepository() = runBlockingTest {
        val id = shoppingList.id
        given(shoppingListRepository.getShoppingListById(id)).willReturn(shoppingList)

        viewModel.loadShoppingList(id)

        assertThat(viewModel.stateLiveData.value).isEqualTo(Loaded(shoppingList))
        assertThat(viewModel.listName.get()).isEqualTo(shoppingList.name)
    }

    @Test
    fun givenRepositoryFailsToLoad_whenLoadShoppingList_thenNavigateUp() = runBlockingTest {
        val id = shoppingList.id
        given(shoppingListRepository.getShoppingListById(id)).willReturn(null)

        viewModel.loadShoppingList(id)

        verify(mainNavigator).navigateUp()
        verify(toastNavigator).show(R.string.list_not_found)
    }

    @Test
    fun givenRepositoryThrows_whenLoadShoppingList_thenPostErrorToLiveData() = runBlockingTest {
        val exception = RuntimeException()
        given(shoppingListRepository.getShoppingListById(any())).willThrow(exception)

        viewModel.loadShoppingList(1)

        assertThat(viewModel.stateLiveData.value).isEqualTo(Error(exception))
    }

    @Test
    fun givenListFailedToLoad_whenRetryLoadShoppingList_thenLoadShoppingListWithId() = runBlockingTest {
        given(shoppingListRepository.getShoppingListById(shoppingList.id)).willThrow(RuntimeException())
        viewModel.loadShoppingList(shoppingList.id)

        viewModel.retryLoadShoppingList()

        verify(shoppingListRepository, times(2)).getShoppingListById(shoppingList.id)
    }

    @Test
    fun givenListFailedToCreate_whenRetryLoadShoppingList_thenRetryCreateShoppingList() = runBlockingTest {
        val name = "new_list"
        given(resourceProvider.getString(R.string.new_list)).willReturn(name)
        given(shoppingListRepository.createNewShoppingList(name)).willThrow(RuntimeException())
        viewModel.createNewShoppingList()

        viewModel.retryLoadShoppingList()

        verify(shoppingListRepository, times(2)).createNewShoppingList(name)
    }

    @Test
    fun givenItemName_whenAddItem_thenItemAddedAndAddItemTextCleared() = runBlockingTest {
        val itemName = "new_item"
        val newShopItems = mutableListOf(aShopItem().withName(itemName).build())
        val newShoppingList = aShoppingList().withItems(newShopItems).build()
        givenShoppingList()
        given(shoppingListRepository.getShoppingListById(shoppingList.id)).willReturn(newShoppingList)

        viewModel.addItem(itemName)

        assertThat(viewModel.addItemText.get()).isEqualTo("")
        assertThat(viewModel.stateLiveData.value).isEqualTo(Loaded(newShoppingList))
        verify(shoppingListRepository).createNewShopItem(shoppingList.id, itemName)
    }

    @Test
    fun givenRepositoryThrows_whenAddItem_thenItemsReverted() = runBlockingTest {
        val name = "new_item"
        givenShoppingList()
        val currentState = viewModel.stateLiveData.value
        given(shoppingListRepository.createNewShopItem(any(), any())).willThrow(RuntimeException())

        viewModel.addItem(name)

        val inOrder = inOrder(shoppingListRepository)
        inOrder.verify(shoppingListRepository).getShoppingListById(shoppingList.id)
        inOrder.verify(shoppingListRepository).createNewShopItem(shoppingList.id, name)
        inOrder.verifyNoMoreInteractions()
        assertThat(viewModel.stateLiveData.value).isEqualTo(currentState)
        verify(toastNavigator).show(R.string.something_went_wrong)
    }

    @Test
    fun givenShopItemWithQuantityGreaterThanOne_whenOnQuantityDown_thenQuantityDecreasedAndViewUpdated() = runBlockingTest {
        val quantity = 5
        val quantityView: TextView = mock()
        shopItem.quantity = quantity

        viewModel.onQuantityDown(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity - 1)
        verify(quantityView).text = shopItem.quantity.toString()
        verify(shoppingListRepository).updateShopItem(shopItem.id, shopItem.name, quantity - 1, shopItem.checked)
    }

    @Test
    fun givenRepositoryThrows_whenOnQuantityDown_thenQuantityReverted() = runBlockingTest {
        val quantity = 5
        val quantityView: TextView = mock()
        shopItem.quantity = quantity
        given(shoppingListRepository.updateShopItem(any(), any(), any(), any())).willThrow(RuntimeException())

        viewModel.onQuantityDown(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity)
        verify(quantityView).text = quantity.toString()
        verify(toastNavigator).show(R.string.something_went_wrong)
    }

    @Test
    fun givenShopItemWithQuantityOne_whenOnQuantityDown_thenQuantityStaysTheSame() = runBlockingTest {
        val quantity = 1
        val quantityView: TextView = mock()
        shopItem.quantity = quantity

        viewModel.onQuantityDown(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity)
        verifyZeroInteractions(quantityView)
        verify(shoppingListRepository, never()).updateShopItem(any(), any(), any(), any())
    }

    @Test
    fun givenShopItem_whenOnQuantityUp_thenQuantityIncreasedAndViewUpdated() = runBlockingTest {
        val quantity = 5
        val quantityView: TextView = mock()
        shopItem.quantity = quantity

        viewModel.onQuantityUp(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity + 1)
        verify(quantityView).text = shopItem.quantity.toString()
        verify(shoppingListRepository).updateShopItem(shopItem.id, shopItem.name, quantity + 1, shopItem.checked)
    }

    @Test
    fun givenRepositoryThrows_whenOnQuantityUp_thenQuantityReverted() = runBlockingTest {
        val quantity = 5
        val quantityView: TextView = mock()
        shopItem.quantity = quantity
        given(shoppingListRepository.updateShopItem(any(), any(), any(), any())).willThrow(RuntimeException())

        viewModel.onQuantityUp(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity)
        verify(quantityView).text = quantity.toString()
        verify(toastNavigator).show(R.string.something_went_wrong)
    }

    @Test
    fun givenShopItem_whenOnDeleteClick_thenItemDeletedFromList() = runBlockingTest {
        givenShoppingList()

        viewModel.onDeleteClick(shopItem)

        val deletedItem = (viewModel.stateLiveData.value as Loaded).shoppingList.items.find {
            it == shopItem
        }
        assertThat(deletedItem).isNull()
        verify(shoppingListRepository).deleteShopItem(shopItem.id)
    }

    @Test
    fun givenRepositoryThrows_whenOnDeleteClick_thenItemNotDeletedFromList() = runBlockingTest {
        givenShoppingList()
        given(shoppingListRepository.deleteShopItem(shopItem.id)).willThrow(RuntimeException())

        viewModel.onDeleteClick(shopItem)

        val deletedItem = (viewModel.stateLiveData.value as Loaded).shoppingList.items.find {
            it == shopItem
        }
        assertThat(deletedItem).isNotNull
        verify(shoppingListRepository).deleteShopItem(shopItem.id)
        verify(toastNavigator).show(R.string.something_went_wrong)
    }

    @Test
    fun givenCheckboxIsChecked_whenOnCheckboxChanged_themShopItemIsChecked() = runBlockingTest {
        val checkBox: CheckBox = mock()
        given(checkBox.isChecked).willReturn(true)
        givenShoppingList()

        viewModel.onCheckboxChecked(checkBox, shopItem)

        val shopItem = (viewModel.stateLiveData.value as Loaded).shoppingList.items.first { it.id == shopItem.id }
        assertThat(shopItem.checked).isTrue
        verify(shoppingListRepository).updateShopItem(shopItem.id, shopItem.name, shopItem.quantity, true)
    }

    @Test
    fun givenCheckboxIsNotChecked_whenOnCheckboxChanged_themShopItemIsNotChecked() = runBlockingTest {
        val checkBox: CheckBox = mock()
        given(checkBox.isChecked).willReturn(false)
        givenShoppingList()

        viewModel.onCheckboxChecked(checkBox, shopItem)

        assertThat(shopItem.checked).isFalse
        verify(shoppingListRepository).updateShopItem(shopItem.id, shopItem.name, shopItem.quantity, false)
    }

    @Test
    fun givenCheckboxIsChecked_whenOnCheckboxChanged_themCheckedItemIsMovedToBottom() = runBlockingTest {
        val checkBox: CheckBox = mock()
        given(checkBox.isChecked).willReturn(true)
        val shopItemWithUniqueId = shopItem.copy(id = 5)
        val shoppingList = aShoppingList().withItems(mutableListOf(shopItem, shopItemWithUniqueId, shopItem)).build()
        givenShoppingList(shoppingList)

        viewModel.onCheckboxChecked(checkBox, shopItemWithUniqueId)

        val lastItem = (viewModel.stateLiveData.value as Loaded).shoppingList.items.last()
        assertThat(lastItem.id).isEqualTo(shopItemWithUniqueId.id)
    }

    @Test
    fun givenRepositoryThrows_whenOnCheckboxChanged_themShopItemCheckDoesNotChange() = runBlockingTest {
        givenShoppingList()
        val checkBox: CheckBox = mock()
        val item = (viewModel.stateLiveData.value as Loaded).shoppingList.items.first()
        val checked = item.checked
        given(checkBox.isChecked).willReturn(checked)
        given(shoppingListRepository.updateShopItem(any(), any(), any(), any())).willThrow(RuntimeException())

        viewModel.onCheckboxChecked(checkBox, item)

        assertThat(item.checked).isEqualTo(checked)
        verify(shoppingListRepository).updateShopItem(item.id, item.name, item.quantity, item.checked)
        verify(toastNavigator).show(R.string.something_went_wrong)
    }

    @Test
    fun givenUpdatedItemIsInList_whenOnNameChanged_thenNameChangedToEditTextValueAndKeyboardHidden() = runBlockingTest {
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
    fun givenUpdatedItemIsNotInList_whenOnNameChanged_thenRepositoryNotUpdated() = runBlockingTest {
        val editText: EditText = mock()
        val editable: Editable = mock()
        val name = "sample_name"
        givenShoppingList()
        given(editable.toString()).willReturn(name)
        given(editText.text).willReturn(editable)
        given(editText.context).willReturn(context)
        given(context.getSystemService(Activity.INPUT_METHOD_SERVICE)).willReturn(imm)

        (viewModel.stateLiveData.value as Loaded).shoppingList.items.remove(shopItem)
        viewModel.onNameChanged(editText, shopItem)

        verify(imm).hideSoftInputFromWindow(eq(editText.windowToken), any())
        verify(shoppingListRepository, never()).updateShopItem(any(), any(), any(), any())
    }

    @Test
    fun givenRepositoryThrows_whenOnNameChanged_thenNameNotChanged() = runBlockingTest {
        val editText: EditText = mock()
        val editable: Editable = mock()
        givenShoppingList()
        val item = (viewModel.stateLiveData.value as Loaded).shoppingList.items.first()
        val oldName = item.name
        val changedName = "sample_name"
        given(editable.toString()).willReturn(changedName)
        given(editText.text).willReturn(editable)
        given(editText.context).willReturn(context)
        given(context.getSystemService(Activity.INPUT_METHOD_SERVICE)).willReturn(imm)
        given(shoppingListRepository.updateShopItem(any(), any(), any(), any())).willThrow(RuntimeException())

        viewModel.onNameChanged(editText, item)

        assertThat(item.name).isEqualTo(oldName)
        verify(imm).hideSoftInputFromWindow(eq(editText.windowToken), any())
        verify(shoppingListRepository).updateShopItem(item.id, changedName, item.quantity, item.checked)
        verify(toastNavigator).show(R.string.something_went_wrong)
    }

    @Test
    fun givenView_whenHideKeyboard_thenKeyboardHidden() {
        given(view.context).willReturn(context)
        given(context.getSystemService(Activity.INPUT_METHOD_SERVICE)).willReturn(imm)

        viewModel.hideKeyboard(view)

        verify(imm).hideSoftInputFromWindow(eq(view.windowToken), any())
    }

    @Test
    fun givenNewName_whenRenameDialogCallback_thenRenameShoppingList() = runBlockingTest {
        givenShoppingList()
        val currentName = (viewModel.stateLiveData.value as Loaded).shoppingList.name
        val newName = "new_name"
        val shoppingListWithNewName = shoppingList.copy(name = newName)
        given(shoppingListRepository.updateShoppingList(shoppingList.id, newName)).willReturn(shoppingListWithNewName)

        viewModel.showRenameDialog()

        val callbackCaptor = argumentCaptor<(String) -> Unit>()
        verify(dialogNavigator).displayRenameDialog(eq(currentName), callbackCaptor.capture(), eq(null), eq(true))
        callbackCaptor.firstValue.invoke(newName)

        assertThat(viewModel.listName.get()).isEqualTo(newName)
        assertThat(viewModel.stateLiveData.value).isEqualTo(Loaded(shoppingListWithNewName))
    }

    @Test
    fun givenUpdateReturnsNull_whenRenameDialogCallback_thenShoppingListNotRenamed() = runBlockingTest {
        givenShoppingList()
        val currentName = (viewModel.stateLiveData.value as Loaded).shoppingList.name
        val newName = "new_name"
        given(shoppingListRepository.updateShoppingList(shoppingList.id, newName)).willReturn(null)

        viewModel.showRenameDialog()

        val callbackCaptor = argumentCaptor<(String) -> Unit>()
        verify(dialogNavigator).displayRenameDialog(eq(currentName), callbackCaptor.capture(), eq(null), eq(true))
        callbackCaptor.firstValue.invoke(newName)

        assertThat(viewModel.listName.get()).isEqualTo(currentName)
        verify(toastNavigator).show(R.string.something_went_wrong)
    }

    @Test
    fun givenRepositoryThrows_whenRenameDialogCallback_thenShoppingListNotRenamed() = runBlockingTest {
        givenShoppingList()
        val currentName = (viewModel.stateLiveData.value as Loaded).shoppingList.name
        val newName = "new_name"
        given(shoppingListRepository.updateShoppingList(any(), any())).willThrow(RuntimeException())

        viewModel.showRenameDialog()

        val callbackCaptor = argumentCaptor<(String) -> Unit>()
        verify(dialogNavigator).displayRenameDialog(eq(currentName), callbackCaptor.capture(), eq(null), eq(true))
        callbackCaptor.firstValue.invoke(newName)

        assertThat(viewModel.listName.get()).isEqualTo(currentName)
        verify(shoppingListRepository).updateShoppingList(any(), eq(newName))
        verify(toastNavigator).show(R.string.something_went_wrong)
    }

    @Test
    fun givenShoppingListWithCheckedItems_whenClearChecked_thenCheckedItemsCleared() = runBlockingTest {
        val uncheckedItem = shopItem.copy(checked = false)
        val checkedItem = shopItem.copy(checked = true)
        givenShoppingList(shoppingList.copy(items = mutableListOf(uncheckedItem, checkedItem)))

        viewModel.clearChecked()

        assertThat((viewModel.stateLiveData.value as Loaded).shoppingList.items).isEqualTo(listOf(uncheckedItem))
    }

    @Test
    fun givenShoppingListWithNoCheckedItems_whenClearChecked_thenNoItemsCleared() = runBlockingTest {
        val uncheckedItem = shopItem.copy(checked = false)
        givenShoppingList(shoppingList.copy(items = mutableListOf(uncheckedItem, uncheckedItem)))

        viewModel.clearChecked()

        assertThat((viewModel.stateLiveData.value as Loaded).shoppingList.items).isEqualTo(listOf(uncheckedItem, uncheckedItem))
    }

    @Test
    fun givenShoppingListWithAllCheckedItems_whenClearChecked_thenAllItemsCleared() = runBlockingTest {
        val checkedItems = shopItem.copy(checked = true)
        givenShoppingList(shoppingList.copy(items = mutableListOf(checkedItems, checkedItems)))

        viewModel.clearChecked()

        assertThat((viewModel.stateLiveData.value as Loaded).shoppingList.items).isEqualTo(emptyList<ShopItem>())
    }

    @Test
    fun givenSwipeRevealLayout_whenOnFocusLost_thenCloseSwipeRevealLayout() {
        val swipeRevealLayout: SwipeRevealLayout = mock()

        viewModel.onFocusLost(swipeRevealLayout)

        verify(swipeRevealLayout).close(true)
    }

    @Test
    fun whenOnBackButtonPressed_thenNavigateUp() {

        viewModel.onBackButtonPressed()

        verify(mainNavigator).navigateUp()
    }

    private fun givenShoppingList(shoppingListOverride: ShoppingList? = null) = runBlockingTest {
        val shoppingList = shoppingListOverride ?: shoppingList
        given(shoppingListRepository.getShoppingListById(shoppingList.id)).willReturn(shoppingList)
        viewModel.loadShoppingList(shoppingList.id)
    }
}
