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
import com.ericthecoder.shopshopshoppinglist.library.extension.notifyObservers
import com.ericthecoder.shopshopshoppinglist.library.livedata.MutableSingleLiveEvent
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel.ViewEvent.NavigateUp
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel.ViewEvent.ShowToast
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.*
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import kotlinx.coroutines.CoroutineExceptionHandler
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

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        viewStateEmitter.postValue(Error(throwable))
    }

    private var listId: Int = -1

    val listName = ObservableField<String>()
    val addItemText = ObservableField<String>()

    fun createNewShoppingList() = viewModelScope.launch(coroutineContextProvider.IO + errorHandler) {
        viewStateEmitter.postValue(Loading)
        val shoppingList = shoppingListRepository.createNewShoppingList(UNNAMED_LIST)
        setShoppingList(shoppingList)
    }

    fun loadShoppingList(id: Int) = viewModelScope.launch(coroutineContextProvider.IO + errorHandler) {
        listId = id
        viewStateEmitter.postValue(Loading)

        val shoppingList = shoppingListRepository.getShoppingListById(id)

        if (shoppingList != null) {
            setShoppingList(shoppingList)
        } else {
            viewEventEmitter.postValue(ShowToast(resourceProvider.getString(R.string.list_not_found)))
            viewEventEmitter.postValue(NavigateUp)
        }
    }

    fun retryLoadShoppingList() =
        if (listId > -1)
            loadShoppingList(listId)
        else
            createNewShoppingList()

    fun addItem(itemName: String) = viewModelScope.launch(coroutineContextProvider.IO) {
        addItemText.set("")
        shoppingList?.items?.add(createTemporaryNewItem(itemName))
        sortListAndPost()

        try {
            shoppingListRepository.createNewShopItem(listId, itemName)
        } catch (e: Exception) {
            shoppingList?.items?.removeLastOrNull()
            sortListAndPost()
            viewEventEmitter.postValue(ShowToast(resourceProvider.getString(R.string.something_went_wrong)))
            return@launch
        }

        val updatedShoppingList = shoppingListRepository.getShoppingListById(listId)

        if (updatedShoppingList != null)
            viewStateEmitter.postValue(Loaded(updatedShoppingList))
        else
            viewEventEmitter.postValue(NavigateUp)
    }

    fun handleArgs(listId: Int) {
        if (this.listId != -1)
            return // ignore: args have been handled

        this.listId = listId
        if (listId != -1)
            loadShoppingList(listId)
        else
            createNewShoppingList()
    }

    private suspend fun setShoppingList(shoppingList: ShoppingList) =
        withContext(coroutineContextProvider.Main) {
            val isNewList = listId == -1
            listId = shoppingList.id
            listName.set(shoppingList.name)
            viewStateEmitter.value = Loaded(shoppingList)

            if (isNewList) { showRenameDialog(NEW_LIST_NAME) }
        }

    private fun createTemporaryNewItem(itemName: String) = ShopItem(-1, itemName, 1, false)

    private fun deleteList() = viewModelScope.launch(coroutineContextProvider.IO) {
        shoppingList?.let { shoppingListRepository.deleteShoppingList(it.id) }
        withContext(coroutineContextProvider.Main) { viewEventEmitter.postValue(NavigateUp) }
    }

    private fun sortListAndPost() {
        shoppingList?.items?.sortBy { it.checked }
        viewStateEmitter.notifyObservers()
    }

    //region: ui interaction events

    override fun onQuantityDown(quantityView: TextView, shopItem: ShopItem) {
        if (shopItem.quantity <= 1) {
            return
        }

        shopItem.quantity -= 1
        quantityView.text = shopItem.quantity.toString()
        viewStateEmitter.notifyObservers()

        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                with(shopItem) { shoppingListRepository.updateShopItem(id, name, quantity, checked) }
            } catch (e: Exception) {
                shopItem.quantity += 1
                quantityView.text = shopItem.quantity.toString()
                viewEventEmitter.postValue(ShowToast(resourceProvider.getString(R.string.something_went_wrong)))
            }
        }
    }

    override fun onQuantityUp(quantityView: TextView, shopItem: ShopItem) {
        shopItem.quantity += 1
        quantityView.text = shopItem.quantity.toString()
        viewStateEmitter.notifyObservers()

        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                with(shopItem) { shoppingListRepository.updateShopItem(id, name, quantity, checked) }
            } catch (e: Exception) {
                shopItem.quantity -= 1
                quantityView.text = shopItem.quantity.toString()
                viewEventEmitter.postValue(ShowToast(resourceProvider.getString(R.string.something_went_wrong)))
            }
        }
    }

    override fun onDeleteClick(shopItem: ShopItem) {
        shoppingList?.items?.removeIf { it.id == shopItem.id }
        sortListAndPost()
        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                shoppingListRepository.deleteShopItem(shopItem.id)
            } catch (e: Exception) {
                shoppingList?.items?.add(shopItem)
                sortListAndPost()
                viewEventEmitter.postValue(ShowToast(resourceProvider.getString(R.string.something_went_wrong)))
            }
        }
    }

    override fun onCheckboxChecked(checkbox: CheckBox, shopItem: ShopItem) {
        if (shopItem.id == -1)
            return

        var item: ShopItem? = null

        getShopItems()?.let { shopItems ->
            val shopItemIndex = shopItems.indexOfFirst { it.id == shopItem.id }
            item = shopItems.firstOrNull { it.id == shopItem.id }?.copy(checked = checkbox.isChecked)
            item?.let { shopItems[shopItemIndex] = it }
            sortListAndPost()
        }

        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                item?.let { shoppingListRepository.updateShopItem(it.id, it.name, it.quantity, it.checked) }
            } catch (e: Exception) {
                withContext(coroutineContextProvider.Main) { checkbox.isChecked = item?.checked ?: false }
                viewEventEmitter.postValue(ShowToast(resourceProvider.getString(R.string.something_went_wrong)))
            }
        }
    }

    override fun onNameChanged(editText: EditText, shopItem: ShopItem) {
        if (shoppingList?.items?.firstOrNull { it.id == shopItem.id } == null)
            return

        val oldName = shopItem.name
        shopItem.name = editText.text.toString()
        viewStateEmitter.notifyObservers()

        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                with(shopItem) { shoppingListRepository.updateShopItem(id, name, quantity, checked) }
            } catch (e: Exception) {
                shopItem.name = oldName
                viewEventEmitter.postValue(ShowToast(resourceProvider.getString(R.string.something_went_wrong)))
            }
        }
    }

    override fun onFocusLost(swipeRevealLayout: SwipeRevealLayout) {
        swipeRevealLayout.close(true)
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showRenameDialog(overrideName: String? = null) = shoppingList?.let {
        val listName = overrideName ?: it.name
        viewEventEmitter.postValue(ViewEvent.DisplayRenameDialog(listName) { newName ->
            renameShoppingList(newName)
        })
    }

    fun showDeleteDialog() = viewState.value?.let {
        val listName = listName.get() ?: return@let
        viewEventEmitter.postValue(ViewEvent.DisplayDeleteDialog(listName) { deleteList() })
    }

    fun onSaveButtonPressed() {
        viewEventEmitter.postValue(ShowToast(String.format(
            resourceProvider.getString(R.string.toast_list_saved), listName.get()
        )))
        viewEventEmitter.postValue(NavigateUp)
    }

    fun clearChecked() = (viewState.value as? Loaded)?.shoppingList?.let { shoppingList ->
        viewModelScope.launch(coroutineContextProvider.IO) {
            shoppingList.items.filter { it.checked }.forEach {
                shoppingListRepository.deleteShopItem(it.id)
                shoppingList.items.remove(it)
            }

            sortListAndPost()
        }
    }

    fun onBackButtonPressed() {
        viewEventEmitter.postValue(NavigateUp)
    }

    //endregion

    private fun renameShoppingList(newName: String) {
        val oldName = listName.get()
        val nameToSet = newName.let { if (it.isBlank()) UNNAMED_LIST else it }

        listName.set(nameToSet)

        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                val shoppingList = shoppingListRepository.updateShoppingList(listId, nameToSet)
                setShoppingList(requireNotNull(shoppingList))
            } catch (e: Exception) {
                listName.set(oldName)
                shoppingList?.let { setShoppingList(it) }
                viewEventEmitter.postValue(ShowToast(resourceProvider.getString(R.string.something_went_wrong)))
                showRenameDialog(nameToSet)
            }
        }
    }

    private fun getShopItems() = (viewState.value as? Loaded)
        ?.shoppingList
        ?.items

    sealed class ViewEvent {
        object NavigateUp : ViewEvent()
        class DisplayRenameDialog(val listTitle: String, val callback: (String) -> Unit) : ViewEvent()
        class DisplayDeleteDialog(val listTitle: String, val callback: () -> Unit) : ViewEvent()
        class ShowToast(val message: String) : ViewEvent()
    }

    companion object {

        internal const val NEW_LIST_NAME = ""
        internal const val UNNAMED_LIST = "Unnamed List"
    }
}
