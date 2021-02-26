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
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.dialogs.DialogNavigator
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.library.extension.notifyObservers
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val mainNavigator: MainNavigator,
    private val dialogNavigator: DialogNavigator
) : ViewModel(), ShopItemEventHandler {

    private val _shoppingListLiveData = MutableLiveData<ShoppingList>()
    val shoppingListLiveData: LiveData<ShoppingList> get() = _shoppingListLiveData

    private var listId: Int = -1

    val listName = ObservableField<String>()
    val addItemText = ObservableField<String>()

    fun createNewShoppingList(context: Context) {
        val newListName = context.getString(R.string.new_list)
        val shoppingList = shoppingListRepository.createNewShoppingList(newListName)
        _shoppingListLiveData.postValue(shoppingList)
    }

    fun loadShoppingList(id: Int) {
        listId = id
        val shoppingList = shoppingListRepository.getShoppingListById(id)

        if (shoppingList != null) {
            listName.set(shoppingList.name)
            _shoppingListLiveData.postValue(shoppingList)
        } else {
            mainNavigator.navigateUp()
        }
    }

    fun addItem(itemName: String) {
        addItemText.set("")
        val newItem = shoppingListRepository.createNewShopItem(listId, itemName)
        shoppingListLiveData.value?.items?.add(newItem)
        _shoppingListLiveData.notifyObservers()
    }

    //region: ui interaction events

    override fun onQuantityDown(quantityView: TextView, shopItem: ShopItem) = with(shopItem) {
        if (quantity > 1) {
            quantity -= 1
            quantityView.text = quantity.toString()
            shoppingListRepository.updateShopItem(id, name, quantity, checked)
        }
    }

    override fun onQuantityUp(quantityView: TextView, shopItem: ShopItem) {
        with(shopItem) {
            quantity += 1
            quantityView.text = quantity.toString()
            shoppingListRepository.updateShopItem(id, name, quantity, checked)
        }
    }

    override fun onDeleteClick(shopItem: ShopItem) {
        shoppingListLiveData.value?.items?.remove(shopItem)
        _shoppingListLiveData.notifyObservers()
        shoppingListRepository.deleteShopItem(shopItem.id)
    }

    override fun onCheckboxChecked(checkbox: CheckBox, shopItem: ShopItem) {
        with(shopItem) {
            checked = checkbox.isChecked
            shoppingListRepository.updateShopItem(id, name, quantity, checked)
        }
    }

    override fun onNameChanged(editText: EditText, shopItem: ShopItem) {
        with(shopItem) {
            name = editText.text.toString()
            hideKeyboard(editText)
            shoppingListRepository.updateShopItem(id, name, quantity, checked)
        }
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showRenameDialog() {
        shoppingListLiveData.value?.let {
            dialogNavigator.displayRenameDialog(
                it.name,
                { newName -> renameShoppingList(newName) }
            )
        }
    }

    //endregion

    private fun renameShoppingList(newName: String) {
        listName.set(newName)
        shoppingListRepository.updateShoppingList(listId, newName)
    }
}
