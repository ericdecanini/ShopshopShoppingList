package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.ericdecanini.entities.ShopItem
import com.ericdecanini.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    viewStateProvider: ViewStateProvider
) : ViewModel() {

    private val state get() = _stateLiveData.value
    private val _stateLiveData = MutableLiveData<HomeViewState>(
        viewStateProvider.create(HomeViewState::class.java)
    )
    val stateLiveData: LiveData<HomeViewState> get() = _stateLiveData

    init {
        _stateLiveData.value = state?.withShoppingLists(generateDummyLists())
    }

    fun navigateToListFragment(navController: NavController)
            = mainNavigator.goToList(navController)


    companion object {
        private fun generateDummyLists(): List<ShoppingList> = listOf(
            ShoppingList("Shoplist Juan", listOf(
                ShopItem.newItem("Oringe"), ShopItem.newItem("Limen"), ShopItem.newItem("Egg Nudels"), ShopItem.newItem("Rais"), ShopItem.newItem("Whine")
            )),
            ShoppingList("Shoplist Toooh", listOf(
                ShopItem.newItem("Oringe"), ShopItem.newItem("Limen"), ShopItem.newItem("Egg Nudels"), ShopItem.newItem("Rais"), ShopItem.newItem("Whine")
            )),
            ShoppingList("Shoplist Tree", listOf(
                ShopItem.newItem("Oringe"), ShopItem.newItem("Limen"), ShopItem.newItem("Egg Nudels"), ShopItem.newItem("Rais"), ShopItem.newItem("Whine")
            ))
        )
    }

}
