package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base

import androidx.navigation.NavController
import com.ericdecanini.shopshopshoppinglist.mvvm.Navigator
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class BaseViewModelTest {

  private val navigator: Navigator = mock()
  private val baseViewModel = ConcreteBaseViewModel(navigator)

  @Test
  fun givenNavController_whenSetControllerForNavigator_thenNavControllerIsSet() {
    val navController: NavController = mock()

    baseViewModel.setControllerForNavigator(navController)

    verify(navigator).navController = navController
  }
}
