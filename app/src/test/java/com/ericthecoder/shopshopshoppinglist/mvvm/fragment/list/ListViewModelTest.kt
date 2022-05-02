package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

import android.text.Editable
import android.widget.CheckBox
import android.widget.EditText
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
import com.ericthecoder.shopshopshoppinglist.util.InstantTaskExecutorExtension
import com.ericthecoder.shopshopshoppinglist.util.TestCoroutineContextProvider
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Suppress("SameParameterValue")
@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class ListViewModelTest {

    private val shoppingListRepository: ShoppingListRepository = mockk(relaxUnitFun = true)
    private val coroutineContextProvider: CoroutineContextProvider = TestCoroutineContextProvider()
    private val resourceProvider: ResourceProvider = mockk()

    private val shopItem = aShopItem().withQuantity(5).build()
    private val itemsList: MutableList<ShopItem> = mutableListOf(shopItem)
    private val shoppingList = aShoppingList(items = itemsList)

    private lateinit var viewModel: ListViewModel

    @BeforeEach
    fun setUp() {
        every { resourceProvider.getString(any()) } returns ""
        every { resourceProvider.getString(any(), *varargAny { true }) } returns ""
        every { resourceProvider.getString(R.string.unnamed_list) } returns UNNAMED_LIST

        viewModel = ListViewModel(
            shoppingListRepository,
            coroutineContextProvider,
            resourceProvider,
        )
    }

    @Nested
    inner class LoadingShoppingList {

        @Test
        fun `loadShoppingList posts loaded view state on success`() {
            coEvery { shoppingListRepository.createNewShoppingList(UNNAMED_LIST) } returns (shoppingList)

            viewModel.loadShoppingList(id = UNSET)

            assertThat(viewModel.viewState.value).isEqualTo(Loaded(shoppingList))
        }

        @Test
        fun `loadShoppingList posts error view state on failure`() {
            val exception = DbQueryFailedException()
            every { resourceProvider.getString(R.string.new_list) } returns ("")
            coEvery { shoppingListRepository.createNewShoppingList(any()) } throws exception

            viewModel.loadShoppingList(id = UNSET)

            assertThat(viewModel.viewState.value).isEqualTo(Error)
        }

        @Test
        fun `reloadShoppingList works for new list`() {
            coEvery { shoppingListRepository.createNewShoppingList(UNNAMED_LIST) } throws RuntimeException()
            viewModel.loadShoppingList(id = UNSET)

            viewModel.reloadShoppingList()

            coVerify { shoppingListRepository.createNewShoppingList(UNNAMED_LIST) }
        }

        @Test
        fun `reloadShoppingList works for existing list`() {
            coEvery { shoppingListRepository.getShoppingListById(shoppingList.id) } throws RuntimeException()
            viewModel.loadShoppingList(shoppingList.id)

            viewModel.reloadShoppingList()

            coVerify { shoppingListRepository.getShoppingListById(shoppingList.id) }
        }
    }

    @Nested
    inner class UpdatingList {

        @Test
        fun `addItem updates shopping list and repository`() {
            val newItem = ShopItem.createNew("new_item")
            givenShoppingList()

            viewModel.addItem(newItem.name)

            val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
            assertThat(viewModel.viewEvent.value).isEqualTo(ResetEditText)
            assertThat(updatedShoppingList.items).contains(newItem)
            coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
        }

        @Test
        fun `addItem trims surrounding whitespace`() {
            val newItem = ShopItem.createNew("new_item")
            givenShoppingList()

            viewModel.addItem(" ${newItem.name} ")

            val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
            assertThat(viewModel.viewEvent.value).isEqualTo(ResetEditText)
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
        fun `adding empty item fails`() {
            givenShoppingList()

            viewModel.addItem("")

            assertThat(viewModel.viewEvent.value).isEqualTo(ShakeAddItemField)
            coVerify(inverse = true) { shoppingListRepository.updateShoppingList(any()) }
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
        fun `when new name is blank, onNameChanged shows error and does not change name`() {
            givenShoppingList()
            val editText: EditText = mockk(relaxUnitFun = true)
            val editable: Editable = mockk()
            val oldName = shopItem.name
            val newName = ""
            every { editable.toString() } returns (newName)
            every { editText.text } returns (editable)

            viewModel.onNameChanged(editText, shopItem)

            assertThat(shopItem.name).isEqualTo(oldName)
            coVerify(inverse = true) { shoppingListRepository.updateShoppingList(any()) }
            assertThat(viewModel.viewEvent.value).isInstanceOf(DisplayGenericDialog::class.java)
        }

        @Test
        fun `onQuantityChanged updates list`() {
            givenShoppingList()
            val mockEditText = mockEditText("5")
            coJustRun { shoppingListRepository.updateShoppingList(shoppingList) }

            viewModel.onQuantityChanged(mockEditText, shoppingList.items.first())

            assertThat(shoppingList.items.first().quantity).isEqualTo(5)
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
        fun `onItemRemoved updates shopping list and repository`() {
            val shopItem1 = aShopItem().withName("shop item one").build()
            val shopItem2 = aShopItem().withName("shop item two").build()
            val items = mutableListOf(shopItem1, shopItem2)
            givenShoppingList(shoppingList.copy(items = items))

            viewModel.onItemRemoved(1)

            val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
            assertThat(updatedShoppingList.items).containsOnly(shopItem1)
            coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
        }

        @Test
        fun `onItemRemoved shows undo snackbar`() {
            givenShoppingList()

            viewModel.onItemRemoved(0)

            assertThat(viewModel.viewEvent.value).isEqualTo(ShowUndoRemoveItemSnackbar(shopItem, 0))
        }

        @Test
        fun `reAddItem adds the item back`() {
            val removedItem = aShopItem().build()
            givenShoppingList()

            viewModel.reAddItem(removedItem, 0)

            val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
            assertThat(updatedShoppingList.items.first()).isEqualTo(removedItem)
            coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
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
    }

    @Nested
    inner class RenameDialog {
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
    }

    @Nested
    inner class ClearCheckedItems {
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
            val currentShoppingList = shoppingList.copy(items = mutableListOf(uncheckedItem, uncheckedItem))
            givenShoppingList(currentShoppingList)

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
    }

    @Nested
    inner class ClearAllItems {

        @Test
        fun `showClearAllDialog shows dialog`() {
            givenShoppingList()

            viewModel.showClearAllDialog()

            assertThat(viewModel.viewEvent.value).isInstanceOf(DisplayClearAllDialog::class.java)
        }

        @Test
        fun `showClearAllDialog callback updates shopping list and repository`() {
            givenShoppingList()

            viewModel.showClearAllDialog()
            (viewModel.viewEvent.value as DisplayClearAllDialog).callback()

            val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
            assertThat(updatedShoppingList.items).isEmpty()
            coVerify { shoppingListRepository.updateShoppingList(updatedShoppingList) }
        }

        @Test
        fun `when list is empty, showClearAllDialog callback does not update repository`() {
            givenShoppingList(shoppingList.copy(items = mutableListOf()))

            viewModel.showClearAllDialog()
            (viewModel.viewEvent.value as DisplayClearAllDialog).callback()

            val updatedShoppingList = (viewModel.viewState.value as Loaded).shoppingList
            assertThat(updatedShoppingList.items).isEmpty()
            coVerify(inverse = true) { shoppingListRepository.updateShoppingList(any()) }
        }
    }

    @Nested
    inner class Events {

        @Test
        fun `onBackButtonPressed emits navigateUp event`() {

            viewModel.onBackButtonPressed()

            assertThat(viewModel.viewEvent.value).isEqualTo(NavigateUp)
        }

        @Test
        fun `onShareButtonClicked emits share event`() {
            givenShoppingList()

            viewModel.onShareButtonClicked()

            assertThat(viewModel.viewEvent.value).isInstanceOf(Share::class.java)
        }

        @Test
        fun `onShareButtonClicked builds share from shopping list`() {
            givenShoppingList()

            viewModel.onShareButtonClicked()

            val shareText = (viewModel.viewEvent.value as Share).text
            assertThat(shareText.lines()[0]).isEqualTo(shoppingList.name)
            assertThat(shareText.lines()[1]).isBlank
            shoppingList.items.forEachIndexed { index, shopItem ->
                assertThat(shareText.lines()[index + 2]).isEqualTo(
                    "${shopItem.quantity} ${shopItem.name}"
                )
            }
        }
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
