package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base.BaseViewModel
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider

class ListViewModel(
    viewStateProvider: ViewStateProvider
) : BaseViewModel() {

    private val state get() = _stateLiveData.value
    private val _stateLiveData = MutableLiveData(
        viewStateProvider.create(ListViewState::class.java)
    )
    val stateLiveData: LiveData<ListViewState> get() = _stateLiveData

    fun loadShoppingList(id: Int) {
        // TODO: Replace with loading list from service
        val shoppingList = loadDummyList()
        _stateLiveData.postValue(ListViewState(shoppingList.title, shoppingList.items))
    }

    //region: UI Interaction events

    val onItemUpdate: (ShopItem, ShopItem) -> Unit = { oldItem, newItem ->
        _stateLiveData.value = state?.replaceItem(
            oldItem,
            newItem
        )
    }

    val onItemDelete: (ShopItem) -> Unit = { item ->
        _stateLiveData.value = state?.deleteItem(item)
    }

    fun onAddItemClick(itemName: String) {
        _stateLiveData.value = state?.addItem(ShopItem.newItem(itemName))
    }

    //endregion

    private fun loadDummyList(): ShoppingList {
        return ShoppingList.generateDummyList()
    }

}
