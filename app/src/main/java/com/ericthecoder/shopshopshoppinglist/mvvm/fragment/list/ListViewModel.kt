package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.exception.DbQueryFailedException
import com.ericthecoder.shopshopshoppinglist.library.livedata.MutableSingleLiveEvent
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.adapter.ShopItemEventHandler
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val resourceProvider: ResourceProvider,
) : ViewModel(), ShopItemEventHandler {

    private val viewStateEmitter = MutableLiveData<ListViewState>(Initial)
    val viewState: LiveData<ListViewState> get() = viewStateEmitter

    private val viewEventEmitter = MutableSingleLiveEvent<ViewEvent>()
    val viewEvent: LiveData<ViewEvent> get() = viewEventEmitter

    private lateinit var shoppingList: ShoppingList
    private var listId = UNSET

    fun loadShoppingList(id: Int) {
        this.listId = id

        if (id == UNSET) {
            startLoadingNewShoppingList()
        } else {
            startLoadingExistingShoppingList(id)
        }
    }

    private fun startLoadingNewShoppingList() {
        displayLoading()

        launchOnIo {
            try {
                loadNewShoppingList()
            } catch (exception: DbQueryFailedException) {
                handleLoadError(exception)
            }
        }
    }

    private fun displayLoading() {
        viewStateEmitter.postValue(Loading)
    }

    private suspend fun loadNewShoppingList() {
        val shoppingList = createNewShoppingList()
        displayShoppingList(shoppingList)
        displayNewListDialog()
    }

    private suspend fun createNewShoppingList(): ShoppingList {
        val unnamedListTitle = resourceProvider.getString(R.string.unnamed_list)
        return shoppingListRepository.createNewShoppingList(unnamedListTitle)
    }

    private fun startLoadingExistingShoppingList(id: Int) {
        displayLoading()
        launchOnIo {
            try {
                loadExistingShoppingList(id)
            } catch (exception: DbQueryFailedException) {
                handleLoadError(exception)
            }
        }
    }

    private suspend fun loadExistingShoppingList(id: Int) {
        val shoppingList = shoppingListRepository.getShoppingListById(id)
        displayShoppingList(shoppingList)
    }

    private fun displayShoppingList(shoppingList: ShoppingList) {
        this.listId = shoppingList.id
        this.shoppingList = shoppingList
        emitShoppingList()
    }

    private fun emitShoppingList() {
        viewStateEmitter.postValue(Loaded(shoppingList))
    }

    private fun displayNewListDialog() {
        viewEventEmitter.postValue(DisplayNewListDialog(
            onNameSet = { newName -> renameShoppingList(newName) }
        ))
    }

    private fun renameShoppingList(newName: String) {
        shoppingList.rename(newName)
        emitShoppingList()
        launchOnIo { shoppingListRepository.updateShoppingList(shoppingList) }
    }

    private fun handleLoadError(exception: Throwable) {
        exception.printStackTrace()
        displayErrorState()
    }

    private fun displayErrorState() {
        viewStateEmitter.postValue(Error)
    }

    fun reloadShoppingList() {
        if (listId == UNSET)
            startLoadingNewShoppingList()
        else
            startLoadingExistingShoppingList(listId)
    }

    fun addItem(itemName: String) = launchOnIo {
        try {
            performAddItem(itemName)
        } catch (exception: DbQueryFailedException) {
            handleWriteError(exception)
        } catch (exception: BlankFieldException) {
            handleBlankNewItem()
        } catch (exception: ItemInListException) {
            handleItemAlreadyInList(itemName)
        }
    }

    private suspend fun performAddItem(itemName: String) {
        validateNewItem(itemName)
        clearItemTextField()
        saveItemInRepository(itemName)
        emitShoppingList()
    }

    private fun validateNewItem(itemName: String) {
        val itemIsInList = shoppingList.items.any { it.name == itemName }
        when {
            itemName.isBlank() -> throw BlankFieldException()
            itemIsInList -> throw ItemInListException()
        }
    }

    private fun clearItemTextField() {
        viewEventEmitter.postValue(ClearEditText)
    }

    private suspend fun saveItemInRepository(itemName: String) {
        val shopItem = ShopItem.createNew(itemName)
        shoppingList.items.add(shopItem)
        shoppingListRepository.updateShoppingList(shoppingList)
    }

    private fun handleWriteError(exception: Throwable) {
        exception.printStackTrace()
        val toastMessage = resourceProvider.getString(R.string.something_went_wrong)
        viewEventEmitter.postValue(ShowToast(toastMessage))
        reloadShoppingList()
    }

    private fun deleteList() = launchOnIo {
        try {
            performDeleteList()
        } catch (exception: DbQueryFailedException) {
            handleWriteError(exception)
        }
    }

    private suspend fun performDeleteList() {
        shoppingListRepository.deleteShoppingList(shoppingList.id)
        viewEventEmitter.postValue(NavigateUp)
    }

    private fun handleBlankNewItem() {
        viewEventEmitter.postValue(SignalBlankAddItem)
    }

    private fun handleItemAlreadyInList(itemName: String) {
        val toastMessage = resourceProvider.getString(R.string.item_already_in_list, itemName, shoppingList.name)
        viewEventEmitter.postValue(ShowToast(toastMessage))
    }

    //region: ui interaction events

    override fun onDeleteClick(shopItem: ShopItem) {
        deleteItemFromShoppingList(shopItem)
        emitShoppingList()
    }

    private fun deleteItemFromShoppingList(shopItem: ShopItem) {
        shoppingList.items.removeIf { it.name == shopItem.name }
    }

    override fun onItemChecked(checkbox: CheckBox, shopItem: ShopItem) {
        shopItem.checked = checkbox.isChecked
        emitShoppingList()
        saveShoppingList()
    }

    override fun onNameChanged(editText: EditText, shopItem: ShopItem) {
        shopItem.name = editText.text.toString()
        saveShoppingList()
    }

    private fun saveShoppingList() = launchOnIo {
        shoppingListRepository.updateShoppingList(shoppingList)
    }

    override fun onQuantityChanged(editText: EditText, shopItem: ShopItem) {
        val newQuantity = editText.text.toString().toIntOrNull() ?: 0
        editText.setText(newQuantity.toString())
        shopItem.quantity = newQuantity
        saveShoppingList()
    }

    fun showRenameDialog() {
        clearFocus()
        postDisplayRenameDialog(shoppingList.name)
    }

    private fun clearFocus() {
        viewEventEmitter.value = ClearFocus
    }

    private fun postDisplayRenameDialog(title: String) {
        viewEventEmitter.postValue(DisplayRenameDialog(title) { newName ->
            renameShoppingList(newName)
        })
    }

    fun showDeleteDialog() {
        val listName = shoppingList.name
        viewEventEmitter.postValue(DisplayDeleteDialog(listName) { deleteList() })
    }

    fun tryClearChecked() = launchOnIo {
        try {
            clearChecked()
        } catch (exception: DbQueryFailedException) {
            handleWriteError(exception)
        }
    }

    private suspend fun clearChecked() {
        deleteCheckedItems(shoppingList)
        emitShoppingList()
    }

    private suspend fun deleteCheckedItems(shoppingList: ShoppingList) {
        shoppingList.items.filter { it.checked }.forEach {
            shoppingList.items.remove(it)
            shoppingListRepository.updateShoppingList(shoppingList)
        }
    }

    fun onBackButtonPressed() {
        navigateUp()
    }

    private fun navigateUp() {
        viewEventEmitter.postValue(NavigateUp)
    }

    fun resetAddItemBackground() {
        viewEventEmitter.value = ResetAddItem
    }

    fun hideKeyboard() {
        viewEventEmitter.value = HideKeyboard
    }

    //endregion

    private fun launchOnIo(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(coroutineContextProvider.IO) {
            block.invoke(this)
        }
    }

    sealed class ViewEvent {
        object NavigateUp : ViewEvent()
        object ClearFocus : ViewEvent()
        object ClearEditText : ViewEvent()
        object SignalBlankAddItem : ViewEvent()
        object ResetAddItem : ViewEvent()
        object HideKeyboard : ViewEvent()
        class DisplayNewListDialog(val onNameSet: (String) -> Unit) : ViewEvent()
        class DisplayRenameDialog(val listTitle: String, val callback: (String) -> Unit) : ViewEvent()
        class DisplayDeleteDialog(val listTitle: String, val callback: () -> Unit) : ViewEvent()
        class ShowToast(val message: String) : ViewEvent()
    }

    inner class ItemInListException : Throwable()

    inner class BlankFieldException : Throwable()

    companion object {
        internal const val UNSET = -1
    }
}
