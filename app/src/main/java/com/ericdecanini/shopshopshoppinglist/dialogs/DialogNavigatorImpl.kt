package com.ericdecanini.shopshopshoppinglist.dialogs

import android.app.Activity
import androidx.fragment.app.FragmentManager

class DialogNavigatorImpl(
    private val fragmentManager: FragmentManager,
    private val activity: Activity
) : DialogNavigator {

  override fun displayAlertDialog(
      title: String?,
      message: String?,
      positiveText: String?,
      positiveOnClick: (() -> Unit)?,
      negativeText: String?,
      negativeOnClick: (() -> Unit)?,
      cancellable: Boolean
  ) {
    TODO("Not yet implemented")
  }

  companion object {
    private const val DIALOG_TAG = "DIALOG_TAG"
  }
}
