package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewState.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.adapter.ShoppingListEventHandler
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    private val shoppingListRepository: ShoppingListRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val persistentStorageReader: PersistentStorageReader,
) : ViewModel(), ShoppingListEventHandler {

    private val _stateLiveData = MutableLiveData<HomeViewState>(Initial)
    val stateLiveData: LiveData<HomeViewState> get() = _stateLiveData

    private val viewEventEmitter = MutableLiveData<ViewEvent>()
    val viewEventLiveData: LiveData<ViewEvent> get() = viewEventEmitter

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

    fun refreshPremiumState() {
        viewEventEmitter.value = ViewEvent.SetHasOptionsMenu(!persistentStorageReader.isPremium())
    }

    fun navigateToListFragment() = mainNavigator.goToList()

    fun navigateToUpsell() = mainNavigator.goToUpsell()

    //region: ui interaction events

    override fun onShoppingListClick(shoppingList: ShoppingList) {
        mainNavigator.goToList(shoppingList)
    }

    //endregion

    sealed class ViewEvent {
        data class SetHasOptionsMenu(val enabled: Boolean) : ViewEvent()
    }
}
