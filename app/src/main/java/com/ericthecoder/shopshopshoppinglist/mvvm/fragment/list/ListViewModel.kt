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
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.*
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.ui.toast.ToastNavigator
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val mainNavigator: MainNavigator,
    private val dialogNavigator: DialogNavigator,
    private val resourceProvider: ResourceProvider,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val toastNavigator: ToastNavigator
) : ViewModel(), ShopItemEventHandler {

    private val _stateLiveData = MutableLiveData<ListViewState>(Initial)
    val stateLiveData: LiveData<ListViewState> get() = _stateLiveData
    private val shoppingList get() = (stateLiveData.value as? Loaded)?.shoppingList

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        _stateLiveData.postValue(Error(throwable))
    }

    private var listId: Int = -1

    val listName = ObservableField<String>()
    val addItemText = ObservableField<String>()

    fun createNewShoppingList() = viewModelScope.launch(coroutineContextProvider.IO + errorHandler) {
        _stateLiveData.postValue(Loading)
        val newListName = resourceProvider.getString(R.string.new_list)
        val shoppingList = shoppingListRepository.createNewShoppingList(newListName)
        setShoppingList(shoppingList)
    }

    fun loadShoppingList(id: Int) = viewModelScope.launch(coroutineContextProvider.IO + errorHandler) {
        listId = id
        _stateLiveData.postValue(Loading)

        val shoppingList = shoppingListRepository.getShoppingListById(id)

        if (shoppingList != null) {
            setShoppingList(shoppingList)
        } else {
            toastNavigator.show(R.string.list_not_found)
            mainNavigator.navigateUp()
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
            toastNavigator.show(R.string.something_went_wrong)
            return@launch
        }

        val updatedShoppingList = shoppingListRepository.getShoppingListById(listId)

        if (updatedShoppingList != null)
            _stateLiveData.postValue(Loaded(updatedShoppingList))
        else
            mainNavigator.navigateUp()
    }

    private fun setShoppingList(shoppingList: ShoppingList) {
        listId = shoppingList.id
        listName.set(shoppingList.name)
        _stateLiveData.postValue(Loaded(shoppingList))
    }

    private fun createTemporaryNewItem(itemName: String) = ShopItem(-1, itemName, 1, false)

    private fun deleteList() = viewModelScope.launch(coroutineContextProvider.IO) {
        shoppingList?.let { shoppingListRepository.deleteShoppingList(it.id) }
        withContext(coroutineContextProvider.Main) { mainNavigator.navigateUp() }
    }

    private fun sortListAndPost() {
        shoppingList?.items?.sortBy { it.checked }
        _stateLiveData.notifyObservers()
    }

    //region: ui interaction events

    override fun onQuantityDown(quantityView: TextView, shopItem: ShopItem) {
        if (shopItem.quantity <= 1) {
            return
        }

        shopItem.quantity -= 1
        quantityView.text = shopItem.quantity.toString()
        _stateLiveData.notifyObservers()

        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                with(shopItem) { shoppingListRepository.updateShopItem(id, name, quantity, checked) }
            } catch (e: Exception) {
                shopItem.quantity += 1
                quantityView.text = shopItem.quantity.toString()
                toastNavigator.show(R.string.something_went_wrong)
            }
        }
    }

    override fun onQuantityUp(quantityView: TextView, shopItem: ShopItem) {
        shopItem.quantity += 1
        quantityView.text = shopItem.quantity.toString()
        _stateLiveData.notifyObservers()

        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                with(shopItem) { shoppingListRepository.updateShopItem(id, name, quantity, checked) }
            } catch (e: Exception) {
                shopItem.quantity -= 1
                quantityView.text = shopItem.quantity.toString()
                toastNavigator.show(R.string.something_went_wrong)
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
                toastNavigator.show(R.string.something_went_wrong)
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
                toastNavigator.show(R.string.something_went_wrong)
            }
        }
    }

    override fun onNameChanged(editText: EditText, shopItem: ShopItem) {
        hideKeyboard(editText)
        if (shoppingList?.items?.firstOrNull { it.id == shopItem.id } == null)
            return

        val oldName = shopItem.name
        shopItem.name = editText.text.toString()
        _stateLiveData.notifyObservers()

        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                with(shopItem) { shoppingListRepository.updateShopItem(id, name, quantity, checked) }
            } catch (e: Exception) {
                shopItem.name = oldName
                toastNavigator.show(R.string.something_went_wrong)
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
        dialogNavigator.displayRenameDialog(
            overrideName ?: it.name,
            { newName -> renameShoppingList(newName) }
        )
    }

    fun showDeleteDialog() = stateLiveData.value?.let {
        val listName = listName.get() ?: return@let

        dialogNavigator.displayGenericDialog(
            title = resourceProvider.getString(R.string.delete),
            message = resourceProvider.getString(R.string.delete_dialog_message, listName),
            positiveText = resourceProvider.getString(R.string.ok),
            positiveOnClick = { deleteList() },
            negativeText = resourceProvider.getString(R.string.cancel),
            negativeOnClick = { /* do nothing */ }
        )
    }

    fun clearChecked() = (stateLiveData.value as? Loaded)?.shoppingList?.let { shoppingList ->
        viewModelScope.launch(coroutineContextProvider.IO) {
            shoppingList.items.filter { it.checked }.forEach {
                shoppingListRepository.deleteShopItem(it.id)
                shoppingList.items.remove(it)
            }

            sortListAndPost()
        }
    }

    fun onBackButtonPressed() {
        mainNavigator.navigateUp()
    }

    //endregion

    private fun renameShoppingList(newName: String) {
        val oldName = listName.get()
        if (!validateRename(newName)) {
            toastNavigator.show(R.string.toast_rename_validation_failed)
            showRenameDialog(newName)
            return
        }

        listName.set(newName)
        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                val shoppingList = shoppingListRepository.updateShoppingList(listId, newName)
                setShoppingList(requireNotNull(shoppingList))
            } catch (e: Exception) {
                listName.set(oldName)
                shoppingList?.let { setShoppingList(it) }
                toastNavigator.show(R.string.something_went_wrong)
                showRenameDialog(newName)
            }
        }
    }

    private fun validateRename(newName: String): Boolean = when {
        newName.isEmpty() -> false
        else -> true
    }

    private fun getShopItems() = (stateLiveData.value as? Loaded)
        ?.shoppingList
        ?.items
}
