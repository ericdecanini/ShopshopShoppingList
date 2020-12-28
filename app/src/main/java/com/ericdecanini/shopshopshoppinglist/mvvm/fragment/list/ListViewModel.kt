package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericdecanini.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.mvvm.viewstate.ListViewState
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider

class ListViewModel(
    viewStateProvider: ViewStateProvider
) : ViewModel() {

    private val state get() = _stateLiveData.value
    private val _stateLiveData = MutableLiveData<ListViewState>(
        viewStateProvider.create(ListViewState::class.java)
    )
    val stateLiveData: LiveData<ListViewState> get() = _stateLiveData

    //region: UI Interaction events

    val onItemNameChanged: (ShopItem, ShopItem) -> Unit = { oldItem, newItem ->
        _stateLiveData.value = state?.replaceListItem(
            oldItem,
            newItem
        )
    }

    fun onAddItemClick(itemName: String) {
        _stateLiveData.value = state?.addNewItem(ShopItem.newItem(itemName))
    }

    //endregion

}
