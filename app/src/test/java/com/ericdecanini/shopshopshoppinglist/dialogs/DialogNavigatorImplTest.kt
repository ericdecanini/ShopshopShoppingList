package com.ericdecanini.shopshopshoppinglist.dialogs

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class DialogNavigatorImplTest {

  private val supportFragmentManager: FragmentManager = mock()
  private val activity: Activity = mock()
  private val dialogNavigator = DialogNavigatorImpl(supportFragmentManager, activity)

  private val fragmentTransaction: FragmentTransaction = mock()

  @Before
  fun setUp() {
    given(supportFragmentManager.beginTransaction()).willReturn(fragmentTransaction)
    given(fragmentTransaction.add(any(), any<String>())).willReturn(fragmentTransaction)
  }

  @Test
  fun givenDialogParams_whenDisplayDialog_thenDialogDisplayedCorrectly() {
    val title = "sample_title"
    val message = "sample_message"
    val positiveText = "sample_positive_text"
    val positiveOnClick: () -> Unit = {}
    val negativeText = "sample_negative_text"
    val negativeOnClick: () -> Unit = {}
    val cancellable = true

    dialogNavigator.displayDialog(title, message, positiveText, positiveOnClick, negativeText, negativeOnClick, cancellable)

    verify(fragmentTransaction).add(any(), any<String>())
  }

}
