package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.util.CoroutineContextProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    private val shoppingListRepository: ShoppingListRepository,
    private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel(), ShoppingListEventHandler {

    private val _shoppingListsLiveData = MutableLiveData<List<ShoppingList>>()
    val shoppingListsLiveData: LiveData<List<ShoppingList>> get() = _shoppingListsLiveData

    fun refreshLists() = viewModelScope.launch(coroutineContextProvider.IO) {
        _shoppingListsLiveData.postValue(shoppingListRepository.getShoppingLists() ?: emptyList())
    }

    fun navigateToListFragment() = mainNavigator.goToList()

    override fun onShoppingListClick(shoppingList: ShoppingList) {
        mainNavigator.goToList(shoppingList)
    }
}
