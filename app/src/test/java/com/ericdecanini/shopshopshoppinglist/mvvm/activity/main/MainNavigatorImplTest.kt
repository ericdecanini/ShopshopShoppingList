package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import androidx.navigation.NavController
import com.ericdecanini.shopshopshoppinglist.R
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class MainNavigatorImplTest {

    private val mainNavigator = MainNavigatorImpl()
    private val navController: NavController = mock()

    @Test
    fun whenGoToList_thenNavigateToListFragment() {
        mainNavigator.goToList(navController)

        verify(navController).navigate(R.id.listFragment)
    }

}
