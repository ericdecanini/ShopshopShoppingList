
package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.database.DbQueryFailedException
import com.ericthecoder.shopshopshoppinglist.library.extension.observeWithMock
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel.Companion.UNSET
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
    private val resourceProvider: ResourceProvider = mockk {
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
            coroutineContextProvider,
        )
    }

    @Test
    fun `startLoadingNewShoppingList posts loaded view state on success`() {
        coEvery { shoppingListRepository.createNewShoppingList(ListViewModel.UNNAMED_LIST_TITLE) } returns (shoppingList)

        viewModel.loadShoppingList(id = UNSET)

        assertThat(viewModel.viewState.value).isEqualTo(Loaded(shoppingList))
    }

    @Test
    fun `startLoadingNewShoppingList posts error view state on failure`() {
        val exception = DbQueryFailedException()
        every { resourceProvider.getString(R.string.new_list) } returns ("")
        coEvery { shoppingListRepository.createNewShoppingList(any()) } throws exception

        viewModel.loadShoppingList(id = UNSET)

        assertThat(viewModel.viewState.value).isEqualTo(Error)
    }

    @Test
    fun `startLoadingExistingShoppingList posts loaded view state on success`() {
        val id = shoppingList.id
        coEvery { shoppingListRepository.getShoppingListById(id) } returns (shoppingList)

        viewModel.loadShoppingList(id)

        assertThat(viewModel.viewState.value).isEqualTo(Loaded(shoppingList))
    }

    @Test
    fun `startLoadingExistingShoppingList posts error view state on failure`() {
        val exception = DbQueryFailedException()
        val id = shoppingList.id
        coEvery { shoppingListRepository.getShoppingListById(id) } throws exception

        viewModel.loadShoppingList(id)

        assertThat(viewModel.viewState.value).isEqualTo(Error)
    }

    @Test
    fun givenRepositoryThrows_whenLoadShoppingList_thenPostErrorToLiveData() {
        val exception = DbQueryFailedException()
        coEvery { shoppingListRepository.getShoppingListById(any()) } throws exception

        viewModel.loadShoppingList(id = 1)

        assertThat(viewModel.viewState.value).isEqualTo(Error)
    }

    @Test
    fun `retryLoadShoppingLists works for existing list`() {
        coEvery { shoppingListRepository.getShoppingListById(shoppingList.id) } throws RuntimeException()
        viewModel.loadShoppingList(shoppingList.id)

        viewModel.reloadShoppingList()

        coVerify { shoppingListRepository.getShoppingListById(shoppingList.id) }
    }

    @Test
    fun `retryLoadShoppingList works for new list`() {
        coEvery { shoppingListRepository.createNewShoppingList(ListViewModel.UNNAMED_LIST_TITLE) } throws RuntimeException()
        viewModel.loadShoppingList(id = UNSET)

        viewModel.reloadShoppingList()

        coVerify { shoppingListRepository.createNewShoppingList(ListViewModel.UNNAMED_LIST_TITLE) }
    }

    @Test
    fun givenItemName_whenAddItem_thenItemAddedAndAddItemTextCleared() {
        val itemName = "new_item"
        val newShopItems = mutableListOf(aShopItem().withName(itemName).build())
        val newShoppingList = aShoppingList().withItems(newShopItems).build()
        givenShoppingList()
        coEvery { shoppingListRepository.createNewShopItem(shoppingList.id, itemName) } returns newShopItems.first()
        coEvery { shoppingListRepository.getShoppingListById(shoppingList.id) } returns newShoppingList

        viewModel.tryAddItem(itemName)

        assertThat(viewModel.viewEvent.value).isEqualTo(ClearEditText)
        assertThat(viewModel.viewState.value).isEqualTo(Loaded(newShoppingList))
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
    }

    @Test
    fun givenCheckboxIsChecked_whenOnCheckboxChanged_themShopItemIsChecked() {
        val checkBox: CheckBox = mockk()
        every { checkBox.isChecked } returns (true)
        givenShoppingList()

        viewModel.onCheckboxChecked(checkBox, shopItem)

        val shopItem = (viewModel.viewState.value as Loaded).shoppingList.items.first { it.id == shopItem.id }
        assertThat(shopItem.checked).isTrue
    }

    @Test
    fun givenCheckboxIsNotChecked_whenOnCheckboxChanged_themShopItemIsNotChecked() {
        val checkBox: CheckBox = mockk()
        every { checkBox.isChecked } returns (false)
        givenShoppingList()

        viewModel.onCheckboxChecked(checkBox, shopItem)

        assertThat(shopItem.checked).isFalse
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
    fun givenUpdatedItemIsInList_whenOnNameChanged_thenNameChangedToEditTextValueAndKeyboardHidden() {
        givenShoppingList()
        val editText: EditText = mockk()
        val editable: Editable = mockk()
        val name = "sample_name"
        every { editable.toString() } returns (name)
        every { editText.text } returns (editable)

        viewModel.onNameChanged(editText, shopItem)

        assertThat(shopItem.name).isEqualTo(name)
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
    fun givenView_whenHideKeyboard_thenKeyboardHidden() {
        every { view.context } returns (context)
        every { context.getSystemService(Activity.INPUT_METHOD_SERVICE) } returns (imm)
        every { view.windowToken } returns mockk()
        every { imm.hideSoftInputFromWindow(any(), any()) } returns true

        viewModel.hideKeyboard(view)

        verify { imm.hideSoftInputFromWindow(any(), any()) }
    }

    @Test
    fun whenShowRenameDialog_thenFocusCleared() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()

        viewModel.showRenameDialog()

        verify { observer.onChanged(ClearFocus) }
    }

    @Test
    fun givenNewName_whenRenameDialogCallback_thenRenameShoppingList() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()
        val newName = "new_name"
        val shoppingListWithNewName = shoppingList.copy(_name = newName)
        coEvery { shoppingListRepository.updateShoppingList(shoppingList.id, newName) } returns (shoppingListWithNewName)

        viewModel.showRenameDialog()

        val slots = mutableListOf<DisplayRenameDialog>()
        verify { observer.onChanged(capture(slots)) }
        slots.last().callback.invoke(newName)

        assertThat(viewModel.viewState.value).isEqualTo(Loaded(shoppingListWithNewName))
    }

    @Test
    fun givenNewNameIsEmpty_whenRenameDialogCallback_thenShoppingListNotRenamed() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()
        val oldName = shoppingList.name
        val newName = ""

        viewModel.showRenameDialog()

        val slots = mutableListOf<DisplayRenameDialog>()
        verify { observer.onChanged(capture(slots)) }
        slots.last().callback.invoke(newName)

        assertThat((viewModel.viewState.value as Loaded).shoppingList.name).isEqualTo(oldName)
    }

    @Test
    fun givenShoppingListWithCheckedItems_whenClearChecked_thenCheckedItemsCleared() {
        val uncheckedItem = shopItem.copy(checked = false)
        val checkedItem = shopItem.copy(checked = true)
        givenShoppingList(shoppingList.copy(items = mutableListOf(uncheckedItem, checkedItem)))
        coEvery { shoppingListRepository.deleteShopItem(any()) } returns mockk()

        viewModel.tryClearChecked()

        assertThat((viewModel.viewState.value as Loaded).shoppingList.items).isEqualTo(listOf(uncheckedItem))
    }

    @Test
    fun givenShoppingListWithNoCheckedItems_whenClearChecked_thenNoItemsCleared() {
        val uncheckedItem = shopItem.copy(checked = false)
        givenShoppingList(shoppingList.copy(items = mutableListOf(uncheckedItem, uncheckedItem)))

        viewModel.tryClearChecked()

        assertThat((viewModel.viewState.value as Loaded).shoppingList.items).isEqualTo(listOf(uncheckedItem, uncheckedItem))
    }

    @Test
    fun givenShoppingListWithAllCheckedItems_whenClearChecked_thenAllItemsCleared() {
        val checkedItems = shopItem.copy(checked = true)
        givenShoppingList(shoppingList.copy(items = mutableListOf(checkedItems, checkedItems)))
        coEvery { shoppingListRepository.deleteShopItem(any()) } returns mockk()

        viewModel.tryClearChecked()

        assertThat((viewModel.viewState.value as Loaded).shoppingList.items).isEqualTo(emptyList<ShopItem>())
    }

    @Test
    fun whenOnBackButtonPressed_thenNavigateUp() {

        viewModel.onBackButtonPressed()

        assertThat(viewModel.viewEvent.value).isEqualTo(NavigateUp)
    }

    private fun givenShoppingList(shoppingListOverride: ShoppingList? = null) {
        val shoppingList = shoppingListOverride ?: shoppingList
        coEvery { shoppingListRepository.getShoppingListById(shoppingList.id) } returns shoppingList
        viewModel.loadShoppingList(shoppingList.id)
    }
}
