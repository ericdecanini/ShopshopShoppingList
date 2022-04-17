package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.library.livedata.MutableSingleLiveEvent
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewState.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.adapter.ShoppingListEventHandler
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val persistentStorageReader: PersistentStorageReader,
    private val persistentStorageWriter: PersistentStorageWriter,
) : ViewModel(), ShoppingListEventHandler {

    private val viewStateEmitter = MutableLiveData<HomeViewState>(Initial)
    val viewState: LiveData<HomeViewState> get() = viewStateEmitter

    private val viewEventEmitter = MutableSingleLiveEvent<ViewEvent>()
    val viewEvent: LiveData<ViewEvent> get() = viewEventEmitter

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        viewStateEmitter.postValue(Error(throwable))
    }

    private var hasChangedTheme = persistentStorageReader.hasChangedTheme()

    init {
        initTitleBreathing()
    }

    private fun initTitleBreathing() {
        if (!hasChangedTheme) {
            viewEventEmitter.value = ViewEvent.SetPlayingBreatheTitle(true)
        }
    }

    fun refreshLists() = viewModelScope.launch(coroutineContextProvider.IO + errorHandler) {
        if (viewState.value !is Loaded
            || (viewState.value as? Loaded)?.items?.isEmpty() == true
        )
            viewStateEmitter.postValue(Loading)

        viewStateEmitter.postValue(
            Loaded(shoppingListRepository.getShoppingLists() ?: emptyList())
        )
    }

    fun refreshPremiumState() {
        viewEventEmitter.value = ViewEvent.SetHasOptionsMenu(
            persistentStorageReader.getPremiumStatus() != PremiumStatus.PREMIUM
        )
    }

    fun navigateToListFragment() {
        viewEventEmitter.value = ViewEvent.OpenList(null)
    }

    fun navigateToUpsell() {
        viewEventEmitter.value = ViewEvent.OpenUpsell
    }

    //region: ui interaction events

    fun onToolbarClick() {
        viewEventEmitter.value = ViewEvent.CycleTheme

        if (!hasChangedTheme) {
            stopTitleBreathing()
        }
    }

    private fun stopTitleBreathing() {
        hasChangedTheme = true
        persistentStorageWriter.setHasChangedTheme(true)
        viewEventEmitter.value = ViewEvent.SetPlayingBreatheTitle(false)
    }

    override fun onShoppingListClick(shoppingList: ShoppingList) {
        viewEventEmitter.value = ViewEvent.OpenList(shoppingList)
    }

    //endregion

    sealed class ViewEvent {
        data class SetHasOptionsMenu(val enabled: Boolean) : ViewEvent()
        data class OpenList(val shoppingList: ShoppingList?) : ViewEvent()
        data class SetPlayingBreatheTitle(val playing: Boolean) : ViewEvent()
        object CycleTheme : ViewEvent()
        object OpenUpsell : ViewEvent()
    }
}
