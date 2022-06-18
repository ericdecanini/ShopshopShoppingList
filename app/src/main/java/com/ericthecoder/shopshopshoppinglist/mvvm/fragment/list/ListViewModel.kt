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
import com.ericthecoder.shopshopshoppinglist.library.extension.moveItem
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
    private var isInitialised = false

    fun loadShoppingList(id: Int) {
        if (!isInitialised) {
            isInitialised = true
            this.listId = id

            if (id == UNSET) {
                startLoadingNewShoppingList()
            } else {
                startLoadingExistingShoppingList(id)
            }
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
        this.shoppingList = shoppingList.apply { emit() }
    }

    private fun ShoppingList.emit() {
        viewStateEmitter.postValue(Loaded(this))
    }

    private fun displayNewListDialog() {
        viewEventEmitter.postValue(DisplayNewListDialog(
            onNameSet = { newName -> renameShoppingList(newName) }
        ))
    }

    private fun renameShoppingList(newName: String) = with(shoppingList) {
        rename(newName)
        emit()
        save()
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

    fun addItem(itemName: String) = itemName.trim().let {
        try {
            performAddItem(it)
        } catch (exception: DbQueryFailedException) {
            handleWriteError(exception)
        } catch (exception: BlankFieldException) {
            handleBlankNewItem()
        } catch (exception: ItemInListException) {
            handleItemAlreadyInList(it)
        }
    }

    private fun performAddItem(itemName: String) {
        validateNewItem(itemName)
        clearItemTextField()
        addAndSaveNewItem(itemName)
        shoppingList.emit()
    }

    private fun validateNewItem(itemName: String) {
        val itemIsInList = shoppingList.items.any { it.name == itemName }
        when {
            itemName.isBlank() -> throw BlankFieldException()
            itemIsInList -> throw ItemInListException()
        }
    }

    private fun clearItemTextField() {
        viewEventEmitter.postValue(ResetEditText)
    }

    private fun addAndSaveNewItem(itemName: String) {
        val shopItem = ShopItem.createNew(itemName)
        shoppingList.items.add(shopItem)
        shoppingList.save()
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
        viewEventEmitter.postValue(ShakeAddItemField)
    }

    private fun handleItemAlreadyInList(itemName: String) {
        val toastMessage = resourceProvider.getString(R.string.item_already_in_list, itemName, shoppingList.name)
        viewEventEmitter.value = ShakeAddItemField
        viewEventEmitter.value = ShowToast(toastMessage)
    }

    //region: ui interaction events

    override fun onDeleteClick(shopItem: ShopItem) {
        deleteItem(shopItem)
        shoppingList.emit()
    }

    private fun deleteItem(shopItem: ShopItem) {
        shoppingList.items.removeIf { it.name == shopItem.name }
        shoppingList.save()
    }

    override fun onItemChecked(checkbox: CheckBox, shopItem: ShopItem) {
        shopItem.checked = checkbox.isChecked
        shoppingList.apply {
            emit()
            save()
        }
    }

    override fun onNameChanged(editText: EditText, shopItem: ShopItem) {
        try {
            val newName = editText.text.toString()
            validateNameChanged(newName)
            setNewItemName(shopItem, newName)
        } catch (exception: BlankFieldException) {
            handleNameChangedError(editText, shopItem)
        }
    }

    private fun validateNameChanged(newName: String) {
        if (newName.isBlank()) {
            throw BlankFieldException()
        }
    }

    private fun setNewItemName(shopItem: ShopItem, newName: String) {
        shopItem.name = newName
        shoppingList.save()
    }

    private fun handleNameChangedError(editText: EditText, shopItem: ShopItem) {
        editText.setText(shopItem.name)
        viewEventEmitter.value = DisplayGenericDialog(
            resourceProvider.getString(R.string.error),
            resourceProvider.getString(R.string.empty_item_name_entered),
        )
    }

    private fun ShoppingList.save() = launchOnIo {
        shoppingListRepository.updateShoppingList(this@save)
    }

    override fun onQuantityChanged(editText: EditText, shopItem: ShopItem) {
        val newQuantity = editText.text.toString().toInt()
        editText.setText(newQuantity.toString())
        shopItem.quantity = newQuantity
        shoppingList.save()
    }

    override fun onItemMoved(from: Int, to: Int) {
        shoppingList.items.moveItem(from, to)
        shoppingList.save()
    }

    override fun onItemRemoved(position: Int) {
        val removedItem = shoppingList.items[position]
        shoppingList.items.remove(removedItem)
        shoppingList.save()
        showUndoRemoveSnackbar(removedItem, position)
    }

    private fun showUndoRemoveSnackbar(removedItem: ShopItem, position: Int) {
        viewEventEmitter.value = ShowUndoRemoveItemSnackbar(removedItem, position)
    }

    fun reAddItem(shopItem: ShopItem, position: Int) {
        shoppingList.items.add(position, shopItem)
        shoppingList.apply {
            emit()
            save()
        }
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
        viewEventEmitter.postValue(DisplayDeleteListDialog(listName) { deleteList() })
    }

    fun clearCheckedItems() {
        try {
            shoppingList.takeIf { deleteCheckedItems() }?.apply {
                emit()
                save()
            }
        } catch (exception: DbQueryFailedException) {
            handleWriteError(exception)
        }
    }

    private fun deleteCheckedItems(): Boolean {
        var wasUpdated = false

        shoppingList.items.filter { it.checked }.forEach {
            shoppingList.items.remove(it)
            wasUpdated = true
        }

        return wasUpdated
    }

    fun showClearAllDialog() {
        viewEventEmitter.postValue(DisplayClearAllDialog(shoppingList.name, ::clearAllItems))
    }

    private fun clearAllItems() {
        try {
            shoppingList.takeIf { deleteAllItems() }?.apply {
                emit()
                save()
            }
        } catch (exception: DbQueryFailedException) {
            handleWriteError(exception)
        }
    }

    private fun deleteAllItems() = if (shoppingList.items.isNotEmpty())  {
        shoppingList.items.clear()
        true
    } else {
        false
    }

    fun onShareButtonClicked() {
        viewEventEmitter.value = Share(buildShoppingListAsText())
    }

    private fun buildShoppingListAsText() = StringBuilder().apply {
        appendLine(shoppingList.name)
        appendLine()
        shoppingList.items.forEach { item ->
            append("${item.quantity} ${item.name}")
            if (item.checked) {
                append(" (checked)")
            }
            appendLine()
        }
    }.toString()

    fun goBack() = viewModelScope.launch {
        if (shoppingList.items.isEmpty()) {
            shoppingListRepository.deleteShoppingList(shoppingList.id)
            viewEventEmitter.value = ShowEmptyListDeletedToast
        }
        viewEventEmitter.value = NavigateUp
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
        object ShowEmptyListDeletedToast : ViewEvent()
        object ClearFocus : ViewEvent()
        object ResetEditText : ViewEvent()
        object HideKeyboard : ViewEvent()
        object ShakeAddItemField : ViewEvent()
        class DisplayGenericDialog(val title: String, val message: String) : ViewEvent()
        class DisplayNewListDialog(val onNameSet: (String) -> Unit) : ViewEvent()
        class DisplayRenameDialog(val listTitle: String, val callback: (String) -> Unit) : ViewEvent()
        class DisplayClearAllDialog(val listTitle: String, val callback: () -> Unit) : ViewEvent()
        class DisplayDeleteListDialog(val listTitle: String, val callback: () -> Unit) : ViewEvent()
        class ShowToast(val message: String) : ViewEvent()
        class Share(val text: String) : ViewEvent()
        data class ShowUndoRemoveItemSnackbar(val item: ShopItem, val position: Int) : ViewEvent()
    }

    inner class ItemInListException : Throwable()

    inner class BlankFieldException : Throwable()

    companion object {
        internal const val UNSET = -1
    }
}
