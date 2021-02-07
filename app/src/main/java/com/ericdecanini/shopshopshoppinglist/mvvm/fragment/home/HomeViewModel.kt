package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.usecases.viewstate.HomeViewState
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    viewStateProvider: ViewStateProvider,
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel(), ShoppingListEventHandler {

    private val state get() = _stateLiveData.value
    private val _stateLiveData = MutableLiveData(
        viewStateProvider.create(HomeViewState::class.java)
    )
    val stateLiveData: LiveData<HomeViewState> get() = _stateLiveData

    init {
        _stateLiveData.value = state?.withShoppingLists(getShoppingLists())
    }

    fun navigateToListFragment() = mainNavigator.goToList()

    private fun getShoppingLists(): List<ShoppingList>
        = shoppingListRepository.getShoppingLists() ?: emptyList()

    //region: ui interaction events

    override fun onShoppingListClick(shoppingList: ShoppingList) {
        mainNavigator.goToList(shoppingList)
    }

    //endregion
}
