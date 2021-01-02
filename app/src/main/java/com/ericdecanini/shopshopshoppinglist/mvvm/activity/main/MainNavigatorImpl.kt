package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import android.os.Bundle
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list.ListFragment

class MainNavigatorImpl : MainNavigator() {

    override fun goToList() = navController.navigate(R.id.listFragment)

    override fun goToList(shoppingList: ShoppingList) {
        val args = Bundle()
        args.putInt(ListFragment.KEY_LIST_ID, shoppingList.id)
        navController.navigate(R.id.listFragment, args)
    }

}
