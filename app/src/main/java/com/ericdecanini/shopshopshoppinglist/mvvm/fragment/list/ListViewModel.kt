package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.library.extension.notifyObservers
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.usecases.viewstate.ListViewState
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import javax.inject.Inject
import kotlin.math.max

class ListViewModel @Inject constructor(
    viewStateProvider: ViewStateProvider,
    private val shoppingListRepository: ShoppingListRepository,
    private val mainNavigator: MainNavigator
) : ViewModel(), ShopItemEventHandler {

    private val state get() = _stateLiveData.value
    private val _stateLiveData = MutableLiveData(
        viewStateProvider.create(ListViewState::class.java)
    )
    val stateLiveData: LiveData<ListViewState> get() = _stateLiveData

    private var listId: Int = -1

    val addItemText = ObservableField<String>()

    fun createNewShoppingList(context: Context) {
        val newListName = context.getString(R.string.new_list)
        val shoppingList = shoppingListRepository.createNewShoppingList(newListName)
        _stateLiveData.postValue(ListViewState(shoppingList))
    }

    fun loadShoppingList(id: Int) {
        listId = id
        val shoppingList = shoppingListRepository.getShoppingListById(id)

        if (shoppingList != null)
            _stateLiveData.postValue(ListViewState(shoppingList))
        else
            mainNavigator.navigateUp()
    }

    fun addItem(itemName: String) {
        addItemText.set("")
        val newItem = shoppingListRepository.createNewShopItem(listId, itemName)
        state?.shoppingList?.items?.add(newItem)
        _stateLiveData.notifyObservers()
    }

    //region: ui interaction events

    override fun onQuantityDown(shopItem: ShopItem) {
        state?.let { state ->
            val index = state.shoppingList.items.indexOf(shopItem)
            if (index > -1) { state.shoppingList.items[index] = ShopItem(shopItem.id, shopItem.name, max(1, shopItem.quantity - 1), shopItem.checked) }
        }
        _stateLiveData.notifyObservers()
    }

    override fun onQuantityUp(shopItem: ShopItem) {
        state?.let { state ->
            val index = state.shoppingList.items.indexOf(shopItem)
            if (index > -1) { state.shoppingList.items[index] = ShopItem(shopItem.id, shopItem.name, shopItem.quantity + 1, shopItem.checked) }
        }
        _stateLiveData.notifyObservers()
    }

    override fun onDeleteClick(shopItem: ShopItem) {
        state?.shoppingList?.items?.remove(shopItem)
        _stateLiveData.notifyObservers()
    }

    override fun onCheckboxChecked(checkbox: CheckBox, shopItem: ShopItem) {
        shopItem.checked = checkbox.isChecked
        _stateLiveData.notifyObservers()
    }

    override fun onNameChanged(editText: EditText, shopItem: ShopItem) {
        shopItem.name = editText.text.toString()
        _stateLiveData.notifyObservers()
        hideKeyboard(editText)
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //endregion
}
