package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ericdecanini.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base.BaseViewModel

class ListViewModel : BaseViewModel() {

    private val shopList = mutableListOf<ShopItem>()

    private val _listLiveData = MutableLiveData<List<ShopItem>>(shopList)
    val listLiveData: LiveData<List<ShopItem>> get() = _listLiveData

    //region: UI Interaction events

     val onItemNameChanged: (ShopItem, String) -> Unit = { shopItem, newName ->
        val index = shopList.indexOf(shopItem)
        shopList[index] = shopItem.copy(name = newName)
    }

    fun onAddItemClick(itemName: String) {
        shopList.add(ShopItem.newItem(itemName))
        _listLiveData.value = shopList
    }

    //endregion

}
