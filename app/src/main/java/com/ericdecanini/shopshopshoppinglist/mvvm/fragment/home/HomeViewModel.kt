package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.databinding.ObservableField
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

    val progressBarVisible = ObservableField(false)

    fun refreshLists() = viewModelScope.launch(coroutineContextProvider.IO) {
        if (shoppingListsLiveData.value?.isEmpty() != false)
            progressBarVisible.set(true)

        _shoppingListsLiveData.postValue(shoppingListRepository.getShoppingLists() ?: emptyList())
        progressBarVisible.set(false)
    }

    fun navigateToListFragment() = mainNavigator.goToList()

    override fun onShoppingListClick(shoppingList: ShoppingList) {
        mainNavigator.goToList(shoppingList)
    }
}
