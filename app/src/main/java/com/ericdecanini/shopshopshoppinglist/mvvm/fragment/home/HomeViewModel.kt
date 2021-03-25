package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home.HomeViewState.*
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home.adapter.ShoppingListEventHandler
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.util.CoroutineContextProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    private val shoppingListRepository: ShoppingListRepository,
    private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel(), ShoppingListEventHandler {

    private val _stateLiveData = MutableLiveData<HomeViewState>(Initial)
    val stateLiveData: LiveData<HomeViewState> get() = _stateLiveData

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        _stateLiveData.postValue(Error(throwable))
    }

    fun refreshLists() = viewModelScope.launch(coroutineContextProvider.IO + errorHandler) {
        if (stateLiveData.value !is Loaded
            || (stateLiveData.value as? Loaded)?.items?.isEmpty() == true
        )
            _stateLiveData.postValue(Loading)

        _stateLiveData.postValue(
            Loaded(shoppingListRepository.getShoppingLists() ?: emptyList())
        )
    }

    fun navigateToListFragment() = mainNavigator.goToList()

    override fun onShoppingListClick(shoppingList: ShoppingList) {
        mainNavigator.goToList(shoppingList)
    }
}
