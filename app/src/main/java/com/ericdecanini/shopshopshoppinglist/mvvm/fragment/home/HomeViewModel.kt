package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList.Companion.generateDummyLists
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    viewStateProvider: ViewStateProvider
) : ViewModel() {

    private val state get() = _stateLiveData.value
    private val _stateLiveData = MutableLiveData(
        viewStateProvider.create(HomeViewState::class.java)
    )
    val stateLiveData: LiveData<HomeViewState> get() = _stateLiveData

    init {
        _stateLiveData.value = state?.withShoppingLists(generateDummyLists())
    }

    val onShoppingListClick: (ShoppingList) -> Unit = { shoppingList ->
        mainNavigator.goToList(shoppingList)
    }

    fun navigateToListFragment() = mainNavigator.goToList()

}
