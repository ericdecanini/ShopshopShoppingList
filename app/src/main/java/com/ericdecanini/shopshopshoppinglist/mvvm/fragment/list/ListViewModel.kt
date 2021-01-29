package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.usecases.viewstate.ListViewState
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import javax.inject.Inject

class ListViewModel @Inject constructor(
    viewStateProvider: ViewStateProvider
) : ViewModel(), ShopItemEventHandler {

    private val state get() = _stateLiveData.value
    private val _stateLiveData = MutableLiveData(
        viewStateProvider.create(ListViewState::class.java)
    )
    val stateLiveData: LiveData<ListViewState> get() = _stateLiveData

    val addItemText = ObservableField<String>()

    fun loadShoppingList(id: Int) {
        // TODO: Replace with loading list from service
        val shoppingList = loadDummyList()
        _stateLiveData.postValue(ListViewState(shoppingList.title, shoppingList.items))
    }

    fun addItem(itemName: String) {
        _stateLiveData.value = state?.addItem(ShopItem.newItem(itemName))
        addItemText.set("")
    }

    private fun updateItem(oldItem: ShopItem, newItem: ShopItem) {
        _stateLiveData.value = state?.replaceItem(oldItem, newItem)
    }

    private fun deleteItem(item: ShopItem) {
        _stateLiveData.value = state?.deleteItem(item)
    }

    //region: ui interaction events

    override fun onQuantityDown(shopItem: ShopItem) {
        updateItem(shopItem, shopItem.withQuantity(shopItem.quantity - 1))
    }

    override fun onQuantityUp(shopItem: ShopItem) {
        updateItem(shopItem, shopItem.withQuantity(shopItem.quantity + 1))
    }

    override fun onDeleteClick(shopItem: ShopItem) {
        deleteItem(shopItem)
    }

    override fun onCheckboxChecked(view: View, shopItem: ShopItem) {
        updateItem(shopItem, shopItem.withChecked((view as CheckBox).isChecked))
    }

    override fun onNameChanged(view: View, shopItem: ShopItem) {
        updateItem(shopItem, shopItem.withName((view as EditText).text.toString()))
    }

    //endregion

    private fun loadDummyList(): ShoppingList {
        return ShoppingList.generateDummyList()
    }

}
