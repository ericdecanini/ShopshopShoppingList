package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

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
import com.ericdecanini.dependencies.android.resources.ResourceProvider
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.library.extension.notifyObservers
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list.ListViewState.*
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list.adapter.ShopItemEventHandler
import com.ericdecanini.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericdecanini.shopshopshoppinglist.ui.toast.ToastNavigator
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.util.CoroutineContextProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
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
        _stateLiveData.notifyObservers()

        try {
            shoppingListRepository.createNewShopItem(listId, itemName)
        } catch (e: Exception) {
            shoppingList?.items?.removeLastOrNull()
            _stateLiveData.notifyObservers()
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
        mainNavigator.navigateUp()
    }

    //region: ui interaction events

    override fun onQuantityDown(quantityView: TextView, shopItem: ShopItem) {
        if (shopItem.quantity <= 1) {
            return
        }

        shopItem.quantity -= 1
        quantityView.text = shopItem.quantity.toString()
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
        shoppingList?.items?.remove(shopItem)
        _stateLiveData.notifyObservers()
        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                shoppingListRepository.deleteShopItem(shopItem.id)
            } catch (e: Exception) {
                shoppingList?.items?.add(shopItem)
                _stateLiveData.notifyObservers()
                toastNavigator.show(R.string.something_went_wrong)
            }
        }
    }

    override fun onCheckboxChecked(checkbox: CheckBox, shopItem: ShopItem) {
        shopItem.checked = checkbox.isChecked
        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                with(shopItem) { shoppingListRepository.updateShopItem(id, name, quantity, checked) }
            } catch (e: Exception) {
                checkbox.isChecked = !checkbox.isChecked
                shopItem.checked = checkbox.isChecked
                toastNavigator.show(R.string.something_went_wrong)
            }
        }
    }

    override fun onNameChanged(editText: EditText, shopItem: ShopItem) {
        hideKeyboard(editText)
        if (shoppingList?.items?.contains(shopItem) == false)
            return

        val oldName = shopItem.name
        shopItem.name = editText.text.toString()
        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                with(shopItem) { shoppingListRepository.updateShopItem(id, name, quantity, checked) }
            } catch (e: Exception) {
                shopItem.name = oldName
                toastNavigator.show(R.string.something_went_wrong)
            }
        }
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showRenameDialog() = shoppingList?.let {
        dialogNavigator.displayRenameDialog(
            it.name,
            { newName -> renameShoppingList(newName) }
        )
    }

    fun showDeleteDialog() = stateLiveData.value?.let {
        val listName = listName.get() ?: resourceProvider.getString(R.string.UNKNOWN_LIST)

        dialogNavigator.displayGenericDialog(
            title = resourceProvider.getString(R.string.delete),
            message = resourceProvider.getString(R.string.delete_dialog_message, listName),
            positiveText = resourceProvider.getString(R.string.ok),
            positiveOnClick = { deleteList() },
            negativeText = resourceProvider.getString(R.string.cancel),
            negativeOnClick = { /* do nothing */ }
        )
    }

    fun onBackButtonPressed() {
        mainNavigator.navigateUp()
    }

    //endregion

    private fun renameShoppingList(newName: String) {
        val oldName = listName.get()
        listName.set(newName)
        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                val shoppingList = shoppingListRepository.updateShoppingList(listId, newName)
                setShoppingList(requireNotNull(shoppingList))
            } catch (e: Exception) {
                listName.set(oldName)
                toastNavigator.show(R.string.something_went_wrong)
            }
        }
    }
}
