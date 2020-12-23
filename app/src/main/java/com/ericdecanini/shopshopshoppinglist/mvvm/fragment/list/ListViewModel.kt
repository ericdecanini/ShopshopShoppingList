package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ericdecanini.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base.BaseViewModel

class ListViewModel : BaseViewModel() {

    private val state get() = _stateLiveData.value
    private val _stateLiveData = MutableLiveData<ListViewState>(ListViewState())
    val stateLiveData: LiveData<ListViewState> get() = _stateLiveData

    //region: UI Interaction events

    val onItemNameChanged: (ShopItem, String) -> Unit = { shopItem, newName ->
        _stateLiveData.value = state?.replaceListItem(
            shopItem,
            ShopItem.newItem(newName)
        )
    }

    fun onAddItemClick(itemName: String) {
        _stateLiveData.value = state?.addNewItem(ShopItem.newItem(itemName))
    }

    //endregion

}
