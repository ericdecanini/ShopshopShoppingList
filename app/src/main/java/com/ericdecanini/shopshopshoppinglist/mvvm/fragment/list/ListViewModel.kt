package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.util.FocusChangeListener
import com.ericdecanini.shopshopshoppinglist.util.ItemCheckedListener
import com.ericdecanini.shopshopshoppinglist.util.ItemClickListener
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import javax.inject.Inject

class ListViewModel @Inject constructor(
    viewStateProvider: ViewStateProvider
) : ViewModel() {

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

    fun createListListeners() = ShopItemListListeners(
        onQuantityDownClick,
        onQuantityUpClick,
        onDeleteClick,
        onCheckboxChecked,
        onNameChanged
    )

    private val onQuantityDownClick = object : ItemClickListener<ShopItem> {
        override fun onItemClicked(item: ShopItem) {
            updateItem(item, item.withQuantity(item.quantity - 1))
        }
    }

    private val onQuantityUpClick = object : ItemClickListener<ShopItem> {
        override fun onItemClicked(item: ShopItem) {
            updateItem(item, item.withQuantity(item.quantity + 1))
        }
    }

    private val onDeleteClick = object : ItemClickListener<ShopItem> {
        override fun onItemClicked(item: ShopItem) {
            deleteItem(item)
        }
    }

    private val onCheckboxChecked = object : ItemCheckedListener<ShopItem> {
        override fun onChecked(view: View, item: ShopItem) {
            updateItem(item, item.withChecked((view as CheckBox).isChecked))
        }
    }

    private val onNameChanged = object : FocusChangeListener<ShopItem> {
        override fun onFocusChanged(view: View, item: ShopItem) {
            updateItem(item, item.withName((view as EditText).text.toString()))
        }
    }

    //endregion

    private fun loadDummyList(): ShoppingList {
        return ShoppingList.generateDummyList()
    }

}
