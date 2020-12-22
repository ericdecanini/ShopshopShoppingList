package com.ericdecanini.shopshopshoppinglist.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ericdecanini.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.base.BaseViewModel

class ListViewModel : BaseViewModel() {

    private val shopList = mutableListOf<ShopItem>()

    private val _listLiveData = MutableLiveData<List<ShopItem>>(shopList)
    val listLiveData: LiveData<List<ShopItem>> get() = _listLiveData

    //region: UI Interaction events

    fun onAddItemClick(itemName: String) {
        shopList.add(ShopItem.newItem(itemName))
        _listLiveData.value = shopList
    }

    //endregion

}
