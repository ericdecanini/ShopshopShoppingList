package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.entities.theme.Theme
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

    private val shoppingLists = mutableListOf<ShoppingList>()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        viewStateEmitter.postValue(Error(throwable))
    }

    fun refreshLists() = viewModelScope.launch(coroutineContextProvider.IO + errorHandler) {
        if (viewState.value !is Loaded
            || (viewState.value as? Loaded)?.items?.isEmpty() == true
        ) {
            viewStateEmitter.postValue(Loading)
        }

        shoppingLists.apply {
            clear()
            shoppingListRepository.getShoppingLists()?.let { addAll(it) }
        }
        viewStateEmitter.postValue(Loaded(shoppingLists))
    }

    fun refreshPremiumState() {
        viewEventEmitter.value = ViewEvent.SetUpsellButtonVisible(
            persistentStorageReader.getPremiumStatus() != PremiumStatus.PREMIUM
        )
    }

    fun navigateToListFragment() {
        viewEventEmitter.value = ViewEvent.OpenList(null)
    }

    fun navigateToUpsell() {
        viewEventEmitter.value = ViewEvent.OpenUpsell
    }

    fun switchToTheme(theme: Theme) {
        persistentStorageWriter.setCurrentTheme(theme.name)
        viewEventEmitter.value = ViewEvent.RecreateActivity
    }

    //region: ui interaction events

    fun onSearchBarTextChanged(searchIndex: String) {
        if (searchIndex.isBlank()) {
            viewStateEmitter.postValue(Loaded(shoppingLists))
        } else {
            val filteredList = shoppingLists.makeCopy().searchTerm(searchIndex)
            viewStateEmitter.postValue(Search(filteredList))
        }
    }

    private fun List<ShoppingList>.makeCopy() = map { it.copy() }

    private fun List<ShoppingList>.searchTerm(term: String): List<ShoppingList> {
        val filteredResults = filter {
            it.nameContains(term) || it.hasItemThatContains(term)
        }
        val titleResults = filteredResults.filter { it.nameContains(term) }
        val itemResults = filteredResults.toMutableList().apply { removeAll(titleResults) }.alterItemSorts(term)
        return titleResults + itemResults
    }

    private fun ShoppingList.nameContains(text: String) = name.contains(text, ignoreCase = true)

    private fun ShoppingList.hasItemThatContains(text: String) = items.any {
        it.name.contains(text, ignoreCase = true)
    }

    private fun List<ShoppingList>.alterItemSorts(term: String) = map { shoppingList ->
        val itemsWithTerm = shoppingList.items.filter { it.name.contains(term, ignoreCase = true) }
        val itemsWithoutTerm = shoppingList.items.apply { removeAll(itemsWithTerm) }
        shoppingList.copy(items = (itemsWithTerm + itemsWithoutTerm).toMutableList())
    }

    override fun onShoppingListClick(shoppingList: ShoppingList) {
        viewEventEmitter.value = ViewEvent.OpenList(shoppingList)
    }

    fun openThemeDialog() {
        viewEventEmitter.value = ViewEvent.OpenThemeDialog
    }

    //endregion

    sealed class ViewEvent {
        data class SetUpsellButtonVisible(val visible: Boolean) : ViewEvent()
        data class OpenList(val shoppingList: ShoppingList?) : ViewEvent()
        object OpenThemeDialog : ViewEvent()
        object OpenUpsell : ViewEvent()
        object RecreateActivity : ViewEvent()
    }
}
