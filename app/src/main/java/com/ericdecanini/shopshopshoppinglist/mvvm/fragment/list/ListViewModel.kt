package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.app.Activity
import android.content.Context
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
import com.ericdecanini.shopshopshoppinglist.dialogs.DialogNavigator
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.library.extension.notifyObservers
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.util.CoroutineContextProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val mainNavigator: MainNavigator,
    private val dialogNavigator: DialogNavigator,
    private val resourceProvider: ResourceProvider,
    private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel(), ShopItemEventHandler {

    private val _shoppingListLiveData = MutableLiveData<ShoppingList>()
    val shoppingListLiveData: LiveData<ShoppingList> get() = _shoppingListLiveData

    private var listId: Int = -1

    val listName = ObservableField<String>()
    val addItemText = ObservableField<String>()

    fun createNewShoppingList(context: Context) = viewModelScope.launch(coroutineContextProvider.IO) {
        val newListName = context.getString(R.string.new_list)
        val shoppingList = shoppingListRepository.createNewShoppingList(newListName)
        setShoppingList(shoppingList)
    }

    fun loadShoppingList(id: Int) = viewModelScope.launch(coroutineContextProvider.IO) {
        val shoppingList = shoppingListRepository.getShoppingListById(id)

        if (shoppingList != null)
            setShoppingList(shoppingList)
        else
            mainNavigator.navigateUp()
    }

    private fun setShoppingList(shoppingList: ShoppingList) {
        listId = shoppingList.id
        listName.set(shoppingList.name)
        _shoppingListLiveData.postValue(shoppingList)
    }

    fun addItem(itemName: String) = viewModelScope.launch(coroutineContextProvider.IO) {
        addItemText.set("")
        val newItem = shoppingListRepository.createNewShopItem(listId, itemName)
        shoppingListLiveData.value?.items?.add(newItem)
        _shoppingListLiveData.notifyObservers()
    }

    private fun deleteList() = shoppingListLiveData.value?.let { shoppingList ->
        viewModelScope.launch {
            withContext(coroutineContextProvider.IO) {
                shoppingListRepository.deleteShoppingList(shoppingList.id)
            }
            mainNavigator.navigateUp()
        }
    } ?: run {
        dialogNavigator.displayGenericDialog(
            title = resourceProvider.getString(R.string.error),
            message = resourceProvider.getString(R.string.something_went_wrong),
            positiveText = resourceProvider.getString(R.string.ok)
        )
    }

    //region: ui interaction events

    override fun onQuantityDown(quantityView: TextView, shopItem: ShopItem) = with(shopItem) {
        if (quantity > 1) {
            quantity -= 1
            quantityView.text = quantity.toString()
            viewModelScope.launch(coroutineContextProvider.IO) {
                shoppingListRepository.updateShopItem(id, name, quantity, checked)
            }
        }
    }

    override fun onQuantityUp(quantityView: TextView, shopItem: ShopItem) {
        with(shopItem) {
            quantity += 1
            quantityView.text = quantity.toString()
            viewModelScope.launch(coroutineContextProvider.IO) {
                shoppingListRepository.updateShopItem(id, name, quantity, checked)
            }
        }
    }

    override fun onDeleteClick(shopItem: ShopItem) {
        shoppingListLiveData.value?.items?.remove(shopItem)
        _shoppingListLiveData.notifyObservers()
        viewModelScope.launch(coroutineContextProvider.IO) {
            shoppingListRepository.deleteShopItem(shopItem.id)
        }
    }

    override fun onCheckboxChecked(checkbox: CheckBox, shopItem: ShopItem) {
        with(shopItem) {
            checked = checkbox.isChecked
            viewModelScope.launch(coroutineContextProvider.IO) {
                shoppingListRepository.updateShopItem(id, name, quantity, checked)
            }
        }
    }

    override fun onNameChanged(editText: EditText, shopItem: ShopItem) {
        with(shopItem) {
            name = editText.text.toString()
            hideKeyboard(editText)
            viewModelScope.launch(coroutineContextProvider.IO) {
                shoppingListRepository.updateShopItem(id, name, quantity, checked)
            }
        }
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showRenameDialog() = shoppingListLiveData.value?.let {
        dialogNavigator.displayRenameDialog(
            it.name,
            { newName -> renameShoppingList(newName) }
        )
    }

    fun showDeleteDialog() = shoppingListLiveData.value?.let {
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

    //endregion

    private fun renameShoppingList(newName: String) {
        listName.set(newName)
        viewModelScope.launch(coroutineContextProvider.IO) {
            shoppingListRepository.updateShoppingList(listId, newName)
        }
    }
}
