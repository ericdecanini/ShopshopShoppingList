
package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

import android.text.Editable
import android.widget.CheckBox
import android.widget.EditText
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.exception.DbQueryFailedException
import com.ericthecoder.shopshopshoppinglist.library.extension.observeWithMock
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel.Companion.UNSET
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.Error
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.Loaded
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShopItemBuilder.Companion.aShopItem
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.aShoppingList
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

    private val shoppingListRepository: ShoppingListRepository = mockk(relaxUnitFun = true)
    private val coroutineContextProvider: CoroutineContextProvider = TestCoroutineContextProvider()
    private val resourceProvider: ResourceProvider = mockk()

    private val shopItem = aShopItem().withQuantity(5).build()
    private val itemsList: MutableList<ShopItem> = mutableListOf(shopItem)
    private val shoppingList = aShoppingList(items = itemsList)

    private lateinit var viewModel: ListViewModel

    @Before
    fun setUp() {
        every { resourceProvider.getString(any(), *varargAny { true }) } returns ""
        every { resourceProvider.getString(R.string.unnamed_list) } returns UNNAMED_LIST

        viewModel = ListViewModel(
            shoppingListRepository,
            coroutineContextProvider,
            resourceProvider,
        )
    }

    @Test
    fun `startLoadingNewShoppingList posts loaded view state on success`() {
        coEvery { shoppingListRepository.createNewShoppingList(UNNAMED_LIST) } returns (shoppingList)

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
        coEvery { shoppingListRepository.createNewShoppingList(UNNAMED_LIST) } throws RuntimeException()
        viewModel.loadShoppingList(id = UNSET)

        viewModel.reloadShoppingList()

        coVerify { shoppingListRepository.createNewShoppingList(UNNAMED_LIST) }
    }

    @Test
    fun `onQuantityChanged updates list`() {
        givenShoppingList()
        val mockEditText = mockEditText("1")
        coJustRun { shoppingListRepository.updateShoppingList(shoppingList) }

        viewModel.onQuantityChanged(mockEditText, shoppingList.items.first())

        coVerify { shoppingListRepository.updateShoppingList(shoppingList) }
    }

    private fun mockEditText(text: String): EditText {
        val mockEditText = mockk<EditText>(relaxUnitFun = true)
        val mockEditable = mockk<Editable>()
        every { mockEditable.toString() } returns text
        every { mockEditText.text } returns mockEditable
        return mockEditText
    }

    @Test
    fun `onItemMoved updates shopping list and repository`() {
        val shopItem1 = aShopItem().withName("shop item one").build()
        val shopItem2 = aShopItem().withName("shop item two").build()
        val items = mutableListOf(shopItem1, shopItem2)
        givenShoppingList(shoppingList.copy(items = items))

        viewModel.onItemMoved(1, 0)

        val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
        assertThat(updatedShoppingList.items).containsSequence(shopItem2, shopItem1)
        coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
    }

    @Test
    fun `addItem updates shopping list and repository`() {
        val newItem = ShopItem.createNew("new_item")
        givenShoppingList()

        viewModel.addItem(newItem.name)

        val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
        assertThat(viewModel.viewEvent.value).isEqualTo(ClearEditText)
        assertThat(updatedShoppingList.items).contains(newItem)
        coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
    }

    @Test
    fun `adding duplicate item fails`() {
        val itemName = "existing_item"
        val existingShopItems = mutableListOf(aShopItem().withName(itemName).build())
        val shoppingListWithItem = aShoppingList(items = existingShopItems)
        givenShoppingList(shoppingListWithItem)

        viewModel.addItem(itemName)

        assertThat(viewModel.viewEvent.value).isInstanceOf(ShowToast::class.java)
        coVerify(inverse = true) { shoppingListRepository.updateShoppingList(any()) }
    }

    @Test
    fun `onDeleteClick updates shopping list and repository`() {
        givenShoppingList()

        viewModel.onDeleteClick(shopItem)

        val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
        val deletedItem = updatedShoppingList.items.find { it == shopItem }
        assertThat(deletedItem).isNull()
        coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
    }

    @Test
    fun `when true onItemChecked updates shopping list and repository`() {
        val checkBox: CheckBox = mockk()
        every { checkBox.isChecked } returns (true)
        givenShoppingList()

        viewModel.onItemChecked(checkBox, shopItem)

        val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
        val shopItem = updatedShoppingList.items.first { it.name == shopItem.name }
        assertThat(shopItem.checked).isTrue
        coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
    }

    @Test
    fun `when false onItemChecked updates shopping list and repository`() {
        val checkBox: CheckBox = mockk()
        every { checkBox.isChecked } returns (false)
        givenShoppingList()

        viewModel.onItemChecked(checkBox, shopItem)

        val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
        val shopItem = updatedShoppingList.items.first { it.name == shopItem.name }
        assertThat(shopItem.checked).isFalse
        coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
    }

    @Test
    fun `onNameChanged updates shopping list and repository`() {
        givenShoppingList()
        val editText: EditText = mockk()
        val editable: Editable = mockk()
        val name = "sample_name"
        every { editable.toString() } returns (name)
        every { editText.text } returns (editable)

        viewModel.onNameChanged(editText, shopItem)

        val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
        assertThat(shopItem.name).isEqualTo(name)
        coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
    }

    @Test
    fun `showing rename dialog clears focus`() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()

        viewModel.showRenameDialog()

        verify { observer.onChanged(ClearFocus) }
    }

    @Test
    fun `rename dialog callbacks updates shopping list and repository`() {
        val observer = viewModel.viewEvent.observeWithMock()
        givenShoppingList()
        val newName = "new_name"

        viewModel.showRenameDialog()
        performRenameDialogCallback(observer, newName)

        val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
        assertThat(updatedShoppingList.name).isEqualTo(newName)
        coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
    }

    private fun performRenameDialogCallback(observer: Observer<ListViewModel.ViewEvent>, newName: String) {
        val slots = mutableListOf<DisplayRenameDialog>()
        verify { observer.onChanged(capture(slots)) }
        slots.last().callback.invoke(newName)
    }

    @Test
    fun `clearCheckedItems updates shopping list and repository`() {
        val uncheckedItem = shopItem.copy(checked = false)
        val checkedItem = shopItem.copy(checked = true)
        givenShoppingList(shoppingList.copy(items = mutableListOf(uncheckedItem, checkedItem)))

        viewModel.clearCheckedItems()

        val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
        assertThat(updatedShoppingList.items).isEqualTo(listOf(uncheckedItem))
        coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
    }

    @Test
    fun `when no items are checked, clearCheckedItems does not update repository`() {
        val uncheckedItem = shopItem.copy(checked = false)
        val currentShoppingList = mutableListOf(uncheckedItem, uncheckedItem)
        givenShoppingList(shoppingList.copy(items = currentShoppingList))

        viewModel.clearCheckedItems()

        val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
        assertThat(updatedShoppingList).isEqualTo(currentShoppingList)
        coVerify(inverse = true) { shoppingListRepository.updateShoppingList(any()) }
    }

    @Test
    fun `when all items are checked, clearCheckedItems clears all items`() {
        val checkedItems = shopItem.copy(checked = true)
        givenShoppingList(shoppingList.copy(items = mutableListOf(checkedItems, checkedItems)))

        viewModel.clearCheckedItems()

        val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
        assertThat(updatedShoppingList.items).isEmpty()
        coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
    }

    @Test
    fun `onBackButtonPressed navigates up`() {

        viewModel.onBackButtonPressed()

        assertThat(viewModel.viewEvent.value).isEqualTo(NavigateUp)
    }

    @Test
    fun `resetAddItemBackground emits reset event`() {

        viewModel.resetAddItemBackground()

        assertThat(viewModel.viewEvent.value).isEqualTo(ResetAddItem)
    }

    private fun givenShoppingList(shoppingListOverride: ShoppingList? = null) {
        val shoppingList = shoppingListOverride ?: shoppingList
        coEvery { shoppingListRepository.getShoppingListById(shoppingList.id) } returns shoppingList
        viewModel.loadShoppingList(shoppingList.id)
    }

    companion object {
        private const val UNNAMED_LIST = "UNNAMED_LIST"
    }
}
