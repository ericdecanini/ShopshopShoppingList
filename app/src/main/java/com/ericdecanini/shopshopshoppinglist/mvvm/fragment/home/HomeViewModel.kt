package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    shoppingListRepository: ShoppingListRepository
) : ViewModel(), ShoppingListEventHandler {

    private val _shoppingListsLiveData = MutableLiveData<List<ShoppingList>>()
    val shoppingListsLiveData: LiveData<List<ShoppingList>> get() = _shoppingListsLiveData

    init {
        _shoppingListsLiveData.value = shoppingListRepository.getShoppingLists() ?: emptyList()
    }

    fun navigateToListFragment() = mainNavigator.goToList()

    //region: ui interaction events

    override fun onShoppingListClick(shoppingList: ShoppingList) {
        mainNavigator.goToList(shoppingList)
    }

    //endregion
}
