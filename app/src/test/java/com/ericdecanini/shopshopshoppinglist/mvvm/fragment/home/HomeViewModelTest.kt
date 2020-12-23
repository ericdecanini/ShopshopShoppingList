package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.navigation.NavController
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class HomeViewModelTest {

    private val mainNavigator: MainNavigator = mock()
    private val viewModel = HomeViewModel(mainNavigator)

    @Test
    fun whenNavigateToListFragment_thenMainNavigatorGoToList() {
        val navController: NavController = mock()

        viewModel.navigateToListFragment(navController)

        verify(mainNavigator).goToList(navController)
    }

}
