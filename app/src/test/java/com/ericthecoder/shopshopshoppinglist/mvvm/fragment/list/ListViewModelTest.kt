
package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

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
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.library.extension.observeWithMock
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.Error
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.Loaded
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShopItemBuilder.Companion.aShopItem
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.Companion.aShoppingList
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.util.TestCoroutineContextProvider
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val shoppingListRepository: ShoppingListRepository = mockk()
    private val resourceProvider: ResourceProvider = mockk() {
        every { getString(any()) } returns ""
    }
    private val coroutineContextProvider: CoroutineContextProvider = TestCoroutineContextProvider()

    private val context: Context = mockk()
    private val view: View = mockk()
    private val imm: InputMethodManager = mockk()

    private val shopItem = aShopItem().withQuantity(5).build()
    private val itemsList: MutableList<ShopItem> = mutableListOf(shopItem)
    private val shoppingList = aShoppingList().withItems(itemsList).build()

    private lateinit var viewModel: ListViewModel

    @Before
    fun setUp() {
        viewModel = ListViewModel(
            shoppingListRepository,
            resourceProvider,
            coroutineContextProvider,
        )
    }

    @Test
    fun givenRepositoryLoads_whenCreateNewShoppingList_thenShoppingListIsCreatedAndPosted() {
        every { resourceProvider.getString(R.string.new_list) } returns (ListViewModel.NEW_LIST_NAME)
        coEvery { shoppingListRepository.createNewShoppingList(ListViewModel.NEW_LIST_NAME) } returns (shoppingList)

        viewModel.createNewShoppingList()

        assertThat(viewModel.viewState.value).isEqualTo(Loaded(shoppingList))
        assertThat(viewModel.listName.get()).isEqualTo(shoppingList.name)
    }

    @Test
    fun givenRepositoryThrows_whenCreateNewShoppingList_thenPostErrorToLiveData() {
        val exception = RuntimeException()
        every { resourceProvider.getString(R.string.new_list) } returns ("")
        coEvery { shoppingListRepository.createNewShoppingList(any()) } throws exception

        viewModel.createNewShoppingList()

        assertThat(viewModel.viewState.value).isEqualTo(Error(exception))
        assertThat(viewModel.listName.get()).isNull()
    }

    @Test
    fun givenRepositoryLoads_whenLoadShoppingList_thenShoppingListLoadedFromRepository() {
        val id = shoppingList.id
        coEvery { shoppingListRepository.getShoppingListById(id) } returns (shoppingList)

        viewModel.loadShoppingList(id)

        assertThat(viewModel.viewState.value).isEqualTo(Loaded(shoppingList))
        assertThat(viewModel.listName.get()).isEqualTo(shoppingList.name)
    }

    @Test
    fun givenRepositoryFailsToLoad_whenLoadShoppingList_thenNavigateUp() {
        val observer = viewModel.viewEvent.observeWithMock()
        val id = shoppingList.id
        coEvery { shoppingListRepository.getShoppingListById(id) } returns (null)

        viewModel.loadShoppingList(id)

        verifyOrder {
            observer.onChanged(any<ShowToast>())
            observer.onChanged(NavigateUp)
        }
    }

    @Test
    fun givenRepositoryThrows_whenLoadShoppingList_thenPostErrorToLiveData() {
        val exception = RuntimeException()
        coEvery { shoppingListRepository.getShoppingListById(any()) } throws exception

        viewModel.loadShoppingList(1)

        assertThat(viewModel.viewState.value).isEqualTo(Error(exception))
    }

    @Test
    fun givenListFailedToLoad_whenRetryLoadShoppingList_thenLoadShoppingListWithId() {
        coEvery { shoppingListRepository.getShoppingListById(shoppingList.id) } throws RuntimeException()
        viewModel.loadShoppingList(shoppingList.id)

        viewModel.retryLoadShoppingList()

        coVerify { shoppingListRepository.getShoppingListById(shoppingList.id) }
    }

    @Test
    fun givenListFailedToCreate_whenRetryLoadShoppingList_thenRetryCreateShoppingList() {
        every { resourceProvider.getString(R.string.new_list) } returns (ListViewModel.NEW_LIST_NAME)
        coEvery { shoppingListRepository.createNewShoppingList(ListViewModel.NEW_LIST_NAME) } throws RuntimeException()
        viewModel.createNewShoppingList()

        viewModel.retryLoadShoppingList()

        coVerify { shoppingListRepository.createNewShoppingList(ListViewModel.NEW_LIST_NAME) }
    }

    @Test
    fun givenItemName_whenAddItem_thenItemAddedAndAddItemTextCleared() {
        val itemName = "new_item"
        val newShopItems = mutableListOf(aShopItem().withName(itemName).build())
        val newShoppingList = aShoppingList().withItems(newShopItems).build()
        givenShoppingList()
        coEvery { shoppingListRepository.getShoppingListById(shoppingList.id) } returns (newShoppingList)
        coEvery { shoppingListRepository.createNewShopItem(shoppingList.id, itemName) } returns mockk()

        viewModel.addItem(itemName)

        assertThat(viewModel.addItemText.get()).isEqualTo("")
        assertThat(viewModel.viewState.value).isEqualTo(Loaded(newShoppingList))
        coVerify { shoppingListRepository.createNewShopItem(shoppingList.id, itemName) }
    }

    @Test
    fun givenRepositoryThrows_whenAddItem_thenItemsReverted() {
        val observer = viewModel.viewEvent.observeWithMock()
        val name = "new_item"
        givenShoppingList()
        val currentState = viewModel.viewState.value
        coEvery { shoppingListRepository.createNewShopItem(any(), any()) } throws RuntimeException()

        viewModel.addItem(name)

        coVerifyOrder {
            shoppingListRepository.getShoppingListById(shoppingList.id)
            shoppingListRepository.createNewShopItem(shoppingList.id, name)
            observer.onChanged(any<ShowToast>())
        }
        assertThat(viewModel.viewState.value).isEqualTo(currentState)
    }

    @Test
    fun givenShopItemWithQuantityGreaterThanOne_whenOnQuantityDown_thenQuantityDecreasedAndViewUpdated() {
        val quantity = 5
        val quantityView: TextView = mockk(relaxUnitFun = true)
        shopItem.quantity = quantity

        viewModel.onQuantityDown(quantityView, shopItem)

        coVerify {
            quantityView.text = (quantity - 1).toString()
            shoppingListRepository.updateShopItem(shopItem.id, shopItem.name, quantity - 1, shopItem.checked)
        }
    }

    @Test
    fun givenRepositoryThrows_whenOnQuantityDown_thenQuantityReverted() {
        val observer = viewModel.viewEvent.observeWithMock()
        val quantity = 5
        val quantityView: TextView = mockk(relaxUnitFun = true)
        shopItem.quantity = quantity
        coEvery { shoppingListRepository.updateShopItem(any(), any(), any(), any()) } throws RuntimeException()

        viewModel.onQuantityDown(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity)
        verify {
            quantityView.text = quantity.toString()
            observer.onChanged(any<ShowToast>())
        }
    }

    @Test
    fun givenShopItemWithQuantityOne_whenOnQuantityDown_thenQuantityStaysTheSame() {
        val quantity = 1
        val quantityView: TextView = mockk()
        shopItem.quantity = quantity

        viewModel.onQuantityDown(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity)
        verify { quantityView wasNot called }
        coVerify(exactly = 0) { shoppingListRepository.updateShopItem(any(), any(), any(), any()) }
    }

    @Test
    fun givenShopItem_whenOnQuantityUp_thenQuantityIncreasedAndViewUpdated() {
        val quantity = 5
        val quantityView: TextView = mockk(relaxUnitFun = true)
        shopItem.quantity = quantity

        viewModel.onQuantityUp(quantityView, shopItem)

        coVerify {
            quantityView.text = (quantity + 1).toString()
            shoppingListRepository.updateShopItem(shopItem.id, shopItem.name, quantity + 1, shopItem.checked)
        }
    }

    @Test
    fun givenRepositoryThrows_whenOnQuantityUp_thenQuantityReverted() {
        val observer = viewModel.viewEvent.observeWithMock()
        val quantity = 5
        val quantityView: TextView = mockk(relaxUnitFun = true)
        shopItem.quantity = quantity
        coEvery { shoppingListRepository.updateShopItem(any(), any(), any(), any()) } throws RuntimeException()

        viewModel.onQuantityUp(quantityView, shopItem)

        assertThat(shopItem.quantity).isEqualTo(quantity)
        verify {
            quantityView.text = quantity.toString()
            observer.onChanged(any<ShowToast>())
        }
    }

    @Test
    fun givenShopItem_whenOnDeleteClick_thenItemDeletedFromList() {
        givenShoppingList()
        coEvery { shoppingListRepository.deleteShopItem(any()) } returns mockk()

        viewModel.onDeleteClick(shopItem)

        val deletedItem = (viewModel.viewState.value as Loaded).shoppingList.items.find {
            it == shopItem
        }
        assertThat(deletedItem).isNull()
        coVerify { shoppingListRepository.deleteShopItem(shopItem.id) }
    }

    @Test
    fun givenRepositoryThrows_whenOnDeleteClick_thenItemNotDeletedFromList() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()
        coEvery { shoppingListRepository.deleteShopItem(shopItem.id) } throws RuntimeException()

        viewModel.onDeleteClick(shopItem)

        val deletedItem = (viewModel.viewState.value as Loaded).shoppingList.items.find {
            it == shopItem
        }
        assertThat(deletedItem).isNotNull
        coVerify {
            shoppingListRepository.deleteShopItem(shopItem.id)
            observer.onChanged(any<ShowToast>())
        }
    }

    @Test
    fun givenCheckboxIsChecked_whenOnCheckboxChanged_themShopItemIsChecked() {
        val checkBox: CheckBox = mockk()
        every { checkBox.isChecked } returns (true)
        givenShoppingList()

        viewModel.onCheckboxChecked(checkBox, shopItem)

        val shopItem = (viewModel.viewState.value as Loaded).shoppingList.items.first { it.id == shopItem.id }
        assertThat(shopItem.checked).isTrue
        coVerify { shoppingListRepository.updateShopItem(shopItem.id, shopItem.name, shopItem.quantity, true) }
    }

    @Test
    fun givenCheckboxIsNotChecked_whenOnCheckboxChanged_themShopItemIsNotChecked() {
        val checkBox: CheckBox = mockk()
        every { checkBox.isChecked } returns (false)
        givenShoppingList()

        viewModel.onCheckboxChecked(checkBox, shopItem)

        assertThat(shopItem.checked).isFalse
        coVerify { shoppingListRepository.updateShopItem(shopItem.id, shopItem.name, shopItem.quantity, false) }
    }

    @Test
    fun givenCheckboxIsChecked_whenOnCheckboxChanged_themCheckedItemIsMovedToBottom() {
        val checkBox: CheckBox = mockk()
        every { checkBox.isChecked } returns (true)
        val shopItemWithUniqueId = shopItem.copy(id = 5)
        val shoppingList = aShoppingList().withItems(mutableListOf(shopItem, shopItemWithUniqueId, shopItem)).build()
        givenShoppingList(shoppingList)

        viewModel.onCheckboxChecked(checkBox, shopItemWithUniqueId)

        val lastItem = (viewModel.viewState.value as Loaded).shoppingList.items.last()
        assertThat(lastItem.id).isEqualTo(shopItemWithUniqueId.id)
    }

    @Test
    fun givenRepositoryThrows_whenOnCheckboxChanged_themShopItemCheckDoesNotChange() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()
        val checkBox: CheckBox = mockk(relaxUnitFun = true)
        val item = (viewModel.viewState.value as Loaded).shoppingList.items.first()
        val checked = item.checked
        every { checkBox.isChecked } returns (checked)
        coEvery { shoppingListRepository.updateShopItem(any(), any(), any(), any()) } throws RuntimeException()

        viewModel.onCheckboxChecked(checkBox, item)

        assertThat(item.checked).isEqualTo(checked)
        coVerify {
            shoppingListRepository.updateShopItem(item.id, item.name, item.quantity, item.checked)
            observer.onChanged(any<ShowToast>())
        }
    }

    @Test
    fun givenUpdatedItemIsInList_whenOnNameChanged_thenNameChangedToEditTextValueAndKeyboardHidden() {
        givenShoppingList()
        val editText: EditText = mockk()
        val editable: Editable = mockk()
        val name = "sample_name"
        every { editable.toString() } returns (name)
        every { editText.text } returns (editable)
        coEvery { shoppingListRepository.updateShopItem(shopItem.id, name, shopItem.quantity, shopItem.checked) } returns shopItem

        viewModel.onNameChanged(editText, shopItem)

        assertThat(shopItem.name).isEqualTo(name)
        coVerify { shoppingListRepository.updateShopItem(shopItem.id, name, shopItem.quantity, shopItem.checked) }
    }

    @Test
    fun givenUpdatedItemIsNotInList_whenOnNameChanged_thenRepositoryNotUpdated() {
        val editText: EditText = mockk()
        val editable: Editable = mockk()
        val name = "sample_name"
        givenShoppingList()
        every { editable.toString() } returns (name)
        every { editText.text } returns (editable)

        (viewModel.viewState.value as Loaded).shoppingList.items.remove(shopItem)
        viewModel.onNameChanged(editText, shopItem)

        coVerify(exactly = 0) { shoppingListRepository.updateShopItem(any(), any(), any(), any()) }
    }

    @Test
    fun givenRepositoryThrows_whenOnNameChanged_thenNameNotChanged() {
        val observer = viewModel.viewEvent.observeWithMock()
        val editText: EditText = mockk()
        val editable: Editable = mockk()
        givenShoppingList()
        val item = (viewModel.viewState.value as Loaded).shoppingList.items.first()
        val oldName = item.name
        val changedName = "sample_name"
        every { editable.toString() } returns (changedName)
        every { editText.text } returns (editable)
        coEvery { shoppingListRepository.updateShopItem(any(), any(), any(), any()) } throws RuntimeException()

        viewModel.onNameChanged(editText, item)

        assertThat(item.name).isEqualTo(oldName)
        coVerify {
            shoppingListRepository.updateShopItem(item.id, changedName, item.quantity, item.checked)
            observer.onChanged(any<ShowToast>())
        }
    }

    @Test
    fun givenView_whenHideKeyboard_thenKeyboardHidden() {
        every { view.context } returns (context)
        every { context.getSystemService(Activity.INPUT_METHOD_SERVICE) } returns (imm)
        every { view.windowToken } returns mockk()
        every { imm.hideSoftInputFromWindow(any(), any()) } returns true

        viewModel.hideKeyboard(view)

        verify { imm.hideSoftInputFromWindow(any(), any()) }
    }

    @Test
    fun givenNewName_whenRenameDialogCallback_thenRenameShoppingList() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()
        val newName = "new_name"
        val shoppingListWithNewName = shoppingList.copy(name = newName)
        coEvery { shoppingListRepository.updateShoppingList(shoppingList.id, newName) } returns (shoppingListWithNewName)

        viewModel.showRenameDialog()

        val slot = slot<DisplayRenameDialog>()
        verify { observer.onChanged(capture(slot)) }
        slot.captured.callback.invoke(newName)

        assertThat(viewModel.listName.get()).isEqualTo(newName)
        assertThat(viewModel.viewState.value).isEqualTo(Loaded(shoppingListWithNewName))
    }

    @Test
    fun givenNewNameIsEmpty_whenRenameDialogCallback_thenShoppingListRenamedToUnnamed() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()
        val newName = ""
        val shoppingListUnnamed = shoppingList.copy(name = ListViewModel.UNNAMED_LIST)
        coEvery { shoppingListRepository.updateShoppingList(shoppingList.id, ListViewModel.UNNAMED_LIST) } returns (shoppingListUnnamed)

        viewModel.showRenameDialog()

        val slot = slot<DisplayRenameDialog>()
        verify { observer.onChanged(capture(slot)) }
        slot.captured.callback.invoke(newName)

        assertThat(viewModel.listName.get()).isEqualTo(ListViewModel.UNNAMED_LIST)
        assertThat(viewModel.viewState.value).isEqualTo(Loaded(shoppingListUnnamed))
    }

    @Test
    fun givenUpdateReturnsNull_whenRenameDialogCallback_thenShoppingListNotRenamed() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()
        val currentName = (viewModel.viewState.value as Loaded).shoppingList.name
        val newName = "new_name"
        coEvery { shoppingListRepository.updateShoppingList(shoppingList.id, newName) } returns (null)

        viewModel.showRenameDialog()

        val slots = mutableListOf<DisplayRenameDialog>()
        verify { observer.onChanged(capture(slots)) }
        verify { observer.onChanged(any<ShowToast>()) }
        slots[0].callback.invoke(newName)
        assertThat(viewModel.listName.get()).isEqualTo(currentName)
        slots.clear()
        verify { observer.onChanged(capture(slots)) }
        assertThat(slots[2].listTitle).isEqualTo(newName)
    }

    @Test
    fun givenRepositoryThrows_whenRenameDialogCallback_thenShoppingListNotRenamed() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()
        val currentName = (viewModel.viewState.value as Loaded).shoppingList.name
        val newName = "new_name"
        coEvery { shoppingListRepository.updateShoppingList(any(), any()) } throws RuntimeException()

        viewModel.showRenameDialog()

        val slot = slot<DisplayRenameDialog>()
        verify { observer.onChanged(capture(slot)) }
        slot.captured.callback.invoke(newName)
        assertThat(viewModel.listName.get()).isEqualTo(currentName)
        coVerify { shoppingListRepository.updateShoppingList(any(), newName) }
        verify { observer.onChanged(any<ShowToast>()) }
    }

    @Test
    fun givenShoppingListWithCheckedItems_whenClearChecked_thenCheckedItemsCleared() {
        val uncheckedItem = shopItem.copy(checked = false)
        val checkedItem = shopItem.copy(checked = true)
        givenShoppingList(shoppingList.copy(items = mutableListOf(uncheckedItem, checkedItem)))
        coEvery { shoppingListRepository.deleteShopItem(any()) } returns mockk()

        viewModel.clearChecked()

        assertThat((viewModel.viewState.value as Loaded).shoppingList.items).isEqualTo(listOf(uncheckedItem))
    }

    @Test
    fun givenShoppingListWithNoCheckedItems_whenClearChecked_thenNoItemsCleared() {
        val uncheckedItem = shopItem.copy(checked = false)
        givenShoppingList(shoppingList.copy(items = mutableListOf(uncheckedItem, uncheckedItem)))

        viewModel.clearChecked()

        assertThat((viewModel.viewState.value as Loaded).shoppingList.items).isEqualTo(listOf(uncheckedItem, uncheckedItem))
    }

    @Test
    fun givenShoppingListWithAllCheckedItems_whenClearChecked_thenAllItemsCleared() {
        val checkedItems = shopItem.copy(checked = true)
        givenShoppingList(shoppingList.copy(items = mutableListOf(checkedItems, checkedItems)))
        coEvery { shoppingListRepository.deleteShopItem(any()) } returns mockk()

        viewModel.clearChecked()

        assertThat((viewModel.viewState.value as Loaded).shoppingList.items).isEqualTo(emptyList<ShopItem>())
    }

    @Test
    fun givenSwipeRevealLayout_whenOnFocusLost_thenCloseSwipeRevealLayout() {
        val swipeRevealLayout: SwipeRevealLayout = mockk(relaxUnitFun = true)

        viewModel.onFocusLost(swipeRevealLayout)

        verify { swipeRevealLayout.close(true) }
    }

    @Test
    fun whenOnBackButtonPressed_thenNavigateUp() {

        viewModel.onBackButtonPressed()

        assertThat(viewModel.viewEvent.value).isEqualTo(NavigateUp)
    }

    @Test
    fun whenOnSavedButtonPressed_thenShowToastAndNavigateUp() {
        val observer = viewModel.viewEvent.observeWithMock()
        every { resourceProvider.getString(R.string.toast_list_saved) } returns ("")

        viewModel.onSaveButtonPressed()

        verify { observer.onChanged(any<ShowToast>()) }
        assertThat(viewModel.viewEvent.value).isEqualTo(NavigateUp)
    }

    private fun givenShoppingList(shoppingListOverride: ShoppingList? = null) {
        val shoppingList = shoppingListOverride ?: shoppingList
        coEvery { shoppingListRepository.getShoppingListById(shoppingList.id) } returns shoppingList
        viewModel.loadShoppingList(shoppingList.id)
    }
}
