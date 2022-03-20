package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.database.DbQueryFailedException
import com.ericthecoder.shopshopshoppinglist.library.extension.notifyObservers
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
) : ViewModel(), ShopItemEventHandler {

    private val viewStateEmitter = MutableLiveData<ListViewState>(Initial)
    val viewState: LiveData<ListViewState> get() = viewStateEmitter

    private val viewEventEmitter = MutableSingleLiveEvent<ViewEvent>()
    val viewEvent: LiveData<ViewEvent> get() = viewEventEmitter

    private lateinit var shoppingList: ShoppingList
    private var listId = UNSET

    fun loadShoppingList(id: Int) {
        this.listId = id

        if (id == UNSET)
            startLoadingNewShoppingList()
        else
            startLoadingExistingShoppingList(id)
    }

    private fun startLoadingNewShoppingList() {
        displayLoading()
        launchOnIo {
            tryLoadNewShoppingList()
        }
    }

    private suspend fun tryLoadNewShoppingList() {
        try {
            loadNewShoppingList()
        } catch (exception: DbQueryFailedException) {
            handleLoadError(exception)
        }
    }

    private suspend fun loadNewShoppingList() {
        val shoppingList = createNewShoppingList()
        displayShoppingList(shoppingList)
        displayNewListDialog()
    }

    private suspend fun createNewShoppingList(): ShoppingList {
        return shoppingListRepository.createNewShoppingList(UNNAMED_LIST_TITLE)
    }

    private fun startLoadingExistingShoppingList(id: Int) {
        displayLoading()
        launchOnIo {
            tryLoadExistingShoppingList(id)
        }
    }

    private suspend fun tryLoadExistingShoppingList(id: Int) {
        try {
            loadExistingShoppingList(id)
        } catch (exception: DbQueryFailedException) {
            handleLoadError(exception)
        }
    }

    private suspend fun loadExistingShoppingList(id: Int) {
        val shoppingList = shoppingListRepository.getShoppingListById(id)
        displayShoppingList(shoppingList)
    }

    private fun displayLoading() {
        viewStateEmitter.postValue(Loading)
    }

    private fun displayShoppingList(shoppingList: ShoppingList) {
        this.listId = shoppingList.id
        this.shoppingList = shoppingList
        emitShoppingList()
    }

    private fun displayNewListDialog() {
        viewEventEmitter.postValue(DisplayNewListDialog(
            onNameSet = { newName -> renameShoppingList(newName) }
        ))
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

    fun tryAddItem(itemName: String) = launchOnIo {
        try {
            addItem(itemName)
        } catch (exception: DbQueryFailedException) {
            handleWriteError(exception)
        }
    }

    private suspend fun addItem(itemName: String) {
        clearItemTextField()
        saveItem(itemName)
        reloadShoppingList()
        sortListAndDisplay()
    }

    private fun clearItemTextField() {
        viewEventEmitter.postValue(ClearEditText)
    }

    private suspend fun saveItem(itemName: String) {
        val shopItem = ShopItem.createNew(itemName)
        shoppingList.items.add(shopItem)
        shoppingListRepository.createNewShopItem(listId, itemName)
    }

    private fun handleWriteError(exception: Throwable) {
        exception.printStackTrace()
        viewEventEmitter.postValue(ShowToast("Something went wrong"))
        reloadShoppingList()
    }

    private fun tryDeleteList() = launchOnIo {
        try {
            deleteList()
        } catch (exception: DbQueryFailedException) {
            handleWriteError(exception)
        }
    }

    private suspend fun deleteList() {
        shoppingListRepository.deleteShoppingList(shoppingList.id)
        viewEventEmitter.postValue(NavigateUp)
    }

    private fun sortListAndDisplay() {
        shoppingList.items.sortBy { it.checked }
        emitShoppingList()
    }

    //region: ui interaction events

    override fun onDeleteClick(shopItem: ShopItem) {
        deleteItemFromShoppingList(shopItem)
        sortListAndDisplay()
    }

    private fun deleteItemFromShoppingList(shopItem: ShopItem) {
        shoppingList.items.removeIf { it.name == shopItem.name }
    }

    override fun onCheckboxChecked(checkbox: CheckBox, shopItem: ShopItem) {
        markItemChecked(checkbox, shopItem)
    }

    private fun markItemChecked(checkbox: CheckBox, shopItem: ShopItem) {
        shoppingList.items.let { shopItems ->
            val shopItemIndex = shopItems.indexOfFirst { it.name == shopItem.name }
            shopItems[shopItemIndex] = shopItem.copy(checked = checkbox.isChecked)
            sortListAndDisplay()
        }
    }

    override fun onNameChanged(editText: EditText, shopItem: ShopItem) {
        if (itemExistsInShoppingList(shopItem)) {
            changeItemName(editText, shopItem)
        }
    }

    private fun itemExistsInShoppingList(shopItem: ShopItem) = shoppingList
        .items
        .firstOrNull { it.name == shopItem.name } != null

    private fun changeItemName(editText: EditText, shopItem: ShopItem) {
        shopItem.name = editText.text.toString()
        viewStateEmitter.notifyObservers()
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
        viewEventEmitter.postValue(DisplayDeleteDialog(listName) { tryDeleteList() })
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
        sortListAndDisplay()
    }

    private suspend fun deleteCheckedItems(shoppingList: ShoppingList) {
        shoppingList.items.filter { it.checked }.forEach {
            shoppingListRepository.deleteShopItem(it.name)
            shoppingList.items.remove(it)
        }
    }

    fun onBackButtonPressed() {
        navigateUp()
    }

    private fun navigateUp() {
        viewEventEmitter.postValue(NavigateUp)
    }

    //endregion

    private fun renameShoppingList(newName: String) {
        renameShoppingListOnUi(newName)
    }

    private fun renameShoppingListOnUi(newName: String) {
        if (newName.isNotBlank()) {
            shoppingList.rename(newName)
            emitShoppingList()
        }
    }

    private fun emitShoppingList() {
        viewStateEmitter.postValue(Loaded(shoppingList))
    }

    private fun launchOnIo(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(coroutineContextProvider.IO) {
            block.invoke(this)
        }
    }

    sealed class ViewEvent {
        object NavigateUp : ViewEvent()
        object ClearFocus : ViewEvent()
        object ClearEditText : ViewEvent()
        class DisplayNewListDialog(val onNameSet: (String) -> Unit) : ViewEvent()
        class DisplayRenameDialog(val listTitle: String, val callback: (String) -> Unit) : ViewEvent()
        class DisplayDeleteDialog(val listTitle: String, val callback: () -> Unit) : ViewEvent()
        class ShowToast(val message: String) : ViewEvent()
    }

    companion object {

        internal const val UNNAMED_LIST_TITLE = "Unnamed List"
        internal const val UNSET = -1
    }
}
