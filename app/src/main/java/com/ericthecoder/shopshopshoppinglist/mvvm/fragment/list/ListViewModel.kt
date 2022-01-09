package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.adapter.ShopItemEventHandler
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.database.DbQueryFailedException
import com.ericthecoder.shopshopshoppinglist.library.extension.notifyObservers
import com.ericthecoder.shopshopshoppinglist.library.livedata.MutableSingleLiveEvent
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.*
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val resourceProvider: ResourceProvider,
    private val coroutineContextProvider: CoroutineContextProvider,
) : ViewModel(), ShopItemEventHandler {

    private val viewStateEmitter = MutableLiveData<ListViewState>(Initial)
    val viewState: LiveData<ListViewState> get() = viewStateEmitter
    private val shoppingList get() = (viewState.value as? Loaded)?.shoppingList

    private val viewEventEmitter = MutableSingleLiveEvent<ViewEvent>()
    val viewEvent: LiveData<ViewEvent> get() = viewEventEmitter

    private var listId = UNSET

    val listName = ObservableField<String>()
    val addItemText = ObservableField<String>()

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
            handleListError(exception)
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
            handleListError(exception)
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
        listId = shoppingList.id
        listName.set(shoppingList.name)
        viewStateEmitter.postValue(Loaded(shoppingList))
    }

    private fun displayNewListDialog() {
        viewEventEmitter.postValue(DisplayNewListDialog(
            onNameSet = { newName -> renameShoppingList(newName) }
        ))
    }

    private fun handleListError(exception: Throwable) {
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

    fun addItem(itemName: String) {
        resetItemTextField()
        addTemporaryItem(itemName)
        sortListAndDisplay()
        launchTryCreateNewItem(itemName)
        reloadShoppingList()
    }

    private fun resetItemTextField() {
        addItemText.set("")
    }

    private fun addTemporaryItem(itemName: String) {
        shoppingList?.items?.add(createTemporaryNewItem(itemName))
    }

    private fun createTemporaryNewItem(itemName: String) = ShopItem(-1, itemName, 1, false)

    private fun launchTryCreateNewItem(itemName: String) {
        launchOnIo {
            tryCreateNewItem(itemName)
        }
    }

    private suspend fun tryCreateNewItem(itemName: String) {
        try {
            createNewItem(itemName)
        } catch (exception: DbQueryFailedException) {
            handleNewItemError(exception)
        }
    }

    private suspend fun createNewItem(itemName: String) {
        shoppingListRepository.createNewShopItem(listId, itemName)
    }

    private fun handleNewItemError(exception: Throwable) {
        removeNewlyAddedItem()
        handleItemError(exception)
    }

    private fun removeNewlyAddedItem() {
        shoppingList?.items?.removeLastOrNull()
        sortListAndDisplay()
    }

    private fun handleItemError(exception: Throwable) {
        exception.printStackTrace()
        viewEventEmitter.postValue(ShowToast(resourceProvider.getString(R.string.something_went_wrong)))
    }

    private fun deleteList() = viewModelScope.launch(coroutineContextProvider.IO) {
        shoppingList?.let { shoppingListRepository.deleteShoppingList(it.id) }
        withContext(coroutineContextProvider.Main) { viewEventEmitter.postValue(NavigateUp) }
    }

    private fun sortListAndDisplay() {
        shoppingList?.items?.sortBy { it.checked }
        viewStateEmitter.notifyObservers()
    }

    //region: ui interaction events

    override fun onQuantityDown(quantityView: TextView, shopItem: ShopItem) {
        if (shopItem.quantity > 1) {
            decrementQuantity(quantityView, shopItem)
        }
    }

    private fun decrementQuantity(quantityView: TextView, shopItem: ShopItem) {
        decrementQuantityOnView(quantityView, shopItem)

        launchOnIo {
            tryDecrementQuantityInRepository(quantityView, shopItem)
        }
    }

    private fun decrementQuantityOnView(quantityView: TextView, shopItem: ShopItem) {
        shopItem.quantity -= 1
        quantityView.text = shopItem.quantity.toString()
        viewStateEmitter.notifyObservers()
    }

    private suspend fun tryDecrementQuantityInRepository(quantityView: TextView, shopItem: ShopItem) {
        try {
            decrementQuantityInRepository(shopItem)
        } catch (exception: DbQueryFailedException) {
            handleDecrementQuantityError(exception, quantityView, shopItem)
        }
    }

    private suspend fun decrementQuantityInRepository(shopItem: ShopItem) {
        with(shopItem) {
            shoppingListRepository.updateShopItem(id, name, quantity, checked)
        }
    }

    private fun handleDecrementQuantityError(exception: Throwable, quantityView: TextView, shopItem: ShopItem) {
        shopItem.quantity += 1
        quantityView.text = shopItem.quantity.toString()
        handleItemError(exception)
    }

    override fun onQuantityUp(quantityView: TextView, shopItem: ShopItem) {
        incrementQuantityOnView(quantityView, shopItem)

        launchOnIo {
            tryIncrementQuantityInRepository(quantityView, shopItem)
        }
    }

    private fun incrementQuantityOnView(quantityView: TextView, shopItem: ShopItem) {
        shopItem.quantity += 1
        quantityView.text = shopItem.quantity.toString()
        viewStateEmitter.notifyObservers()
    }

    private suspend fun tryIncrementQuantityInRepository(quantityView: TextView, shopItem: ShopItem) {
        try {
            incrementQuantityInRepository(shopItem)
        } catch (exception: DbQueryFailedException) {
            handleIncrementQuantityError(exception, quantityView, shopItem)
        }
    }

    private suspend fun incrementQuantityInRepository(shopItem: ShopItem) {
        with(shopItem) {
            shoppingListRepository.updateShopItem(id, name, quantity, checked)
        }
    }

    private fun handleIncrementQuantityError(exception: Throwable, quantityView: TextView, shopItem: ShopItem) {
        shopItem.quantity -= 1
        quantityView.text = shopItem.quantity.toString()
        handleItemError(exception)
    }

    override fun onDeleteClick(shopItem: ShopItem) {
        deleteShopItemOnUi(shopItem)
        deleteShopItemInRepository(shopItem)
    }

    private fun deleteShopItemOnUi(shopItem: ShopItem) {
        deleteItemFromShoppingList(shopItem)
        sortListAndDisplay()
    }

    private fun deleteItemFromShoppingList(shopItem: ShopItem) {
        shoppingList?.items?.removeIf { it.id == shopItem.id }
    }

    private fun deleteShopItemInRepository(shopItem: ShopItem) {
        launchOnIo {
            shoppingListRepository.deleteShopItem(shopItem.id)
        }
    }

    override fun onCheckboxChecked(checkbox: CheckBox, shopItem: ShopItem) {
        if (shopItem.id != UNSET) {
            markItemChecked(checkbox, shopItem)
        }
    }

    private fun markItemChecked(checkbox: CheckBox, shopItem: ShopItem) {
        markItemCheckedInUi(checkbox, shopItem)

        launchOnIo {
            tryMarkItemCheckedInRepository(checkbox, shopItem)
        }
    }

    private fun markItemCheckedInUi(checkbox: CheckBox, shopItem: ShopItem) {
        getShopItems()?.let { shopItems ->
            val shopItemIndex = shopItems.indexOfFirst { it.id == shopItem.id }
            shopItems[shopItemIndex] = shopItem.copy(checked = checkbox.isChecked)
            sortListAndDisplay()
        }
    }

    private suspend fun tryMarkItemCheckedInRepository(checkbox: CheckBox, shopItem: ShopItem) {
        try {
            markItemCheckedInRepository(checkbox, shopItem)
        } catch (exception: DbQueryFailedException) {
            handleCheckItemError(exception, checkbox, shopItem)
        }
    }

    private suspend fun markItemCheckedInRepository(checkbox: CheckBox, shopItem: ShopItem) {
        shoppingListRepository.updateShopItem(shopItem.id, shopItem.name, shopItem.quantity, checkbox.isChecked)
    }

    private suspend fun handleCheckItemError(exception: Throwable, checkbox: CheckBox, shopItem: ShopItem) {
        withContext(coroutineContextProvider.Main) {
            checkbox.isChecked = shopItem.checked
        }
        handleItemError(exception)
    }

    override fun onNameChanged(editText: EditText, shopItem: ShopItem) {
        if (itemExistsInShoppingList(shopItem)) {
            changeItemName(editText, shopItem)
        }
    }

    private fun itemExistsInShoppingList(shopItem: ShopItem) = shoppingList
        ?.items
        ?.firstOrNull { it.id == shopItem.id } != null

    private fun changeItemName(editText: EditText, shopItem: ShopItem) {
        changeItemNameOnUi(editText, shopItem)

        launchOnIo {
            tryChangeItemNameInRepository(shopItem)
        }
    }

    private fun changeItemNameOnUi(editText: EditText, shopItem: ShopItem) {
        shopItem.name = editText.text.toString()
        viewStateEmitter.notifyObservers()
    }

    private suspend fun tryChangeItemNameInRepository(shopItem: ShopItem) {
        try {
            changeItemNameInRepository(shopItem)
        } catch (exception: DbQueryFailedException) {
            handleItemError(exception)
        }
    }

    private suspend fun changeItemNameInRepository(shopItem: ShopItem) {
        with(shopItem) {
            shoppingListRepository.updateShopItem(id, name, quantity, checked)
        }
    }

    override fun onFocusLost(swipeRevealLayout: SwipeRevealLayout) {
        swipeRevealLayout.close(true)
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showRenameDialog() = shoppingList?.let {
        clearFocus()
        postDisplayRenameDialog(it.name)
    }

    private fun clearFocus() {
        viewEventEmitter.value = ClearFocus
    }

    private fun postDisplayRenameDialog(title: String) {
        viewEventEmitter.postValue(DisplayRenameDialog(title) { newName ->
            renameShoppingList(newName)
        })
    }

    fun showDeleteDialog() = viewState.value?.let {
        val listName = listName.get() ?: return@let
        viewEventEmitter.postValue(DisplayDeleteDialog(listName) { deleteList() })
    }

    fun clearChecked() = shoppingList?.let { shoppingList ->
        launchOnIo {
            deleteCheckedItems(shoppingList)
            sortListAndDisplay()
        }
    }

    private suspend fun deleteCheckedItems(shoppingList: ShoppingList) {
        shoppingList.items.filter { it.checked }.forEach {
            shoppingListRepository.deleteShopItem(it.id)
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
        val oldName = shoppingList?.name ?: return
        renameShoppingListOnUi(newName)

        launchOnIo {
            tryRenameShoppingListInRepository(oldName, newName)
        }
    }

    private fun renameShoppingListOnUi(newName: String) {
        val nameToSet = if (newName.isBlank()) UNNAMED_LIST_TITLE else newName
        listName.set(nameToSet)
    }

    private suspend fun tryRenameShoppingListInRepository(oldName: String, newName: String) {
        try {
            renameShoppingListInRepository(newName)
        } catch (exception: DbQueryFailedException) {
            handleRenameListError(exception, oldName)
        }
    }

    private suspend fun renameShoppingListInRepository(newName: String) {
        val nameToSet = if (newName.isBlank()) UNNAMED_LIST_TITLE else newName
        val shoppingList = shoppingListRepository.updateShoppingList(listId, nameToSet)
        displayShoppingList(shoppingList)
    }

    private fun handleRenameListError(exception: Throwable, oldName: String) {
        handleItemError(exception)
        listName.set(oldName)
        showRenameDialog()
    }

    private fun getShopItems() = (viewState.value as? Loaded)
        ?.shoppingList
        ?.items

    private fun launchOnIo(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(coroutineContextProvider.IO) {
            block.invoke(this)
        }
    }

    sealed class ViewEvent {
        object NavigateUp : ViewEvent()
        object ClearFocus : ViewEvent()
        class DisplayNewListDialog(val onNameSet: (String) -> Unit) : ViewEvent()
        class DisplayRenameDialog(val listTitle: String, val callback: (String) -> Unit) :
            ViewEvent()

        class DisplayDeleteDialog(val listTitle: String, val callback: () -> Unit) : ViewEvent()
        class ShowToast(val message: String) : ViewEvent()
    }

    companion object {

        internal const val UNNAMED_LIST_TITLE = "Unnamed List"
        internal const val UNSET = -1
    }
}
