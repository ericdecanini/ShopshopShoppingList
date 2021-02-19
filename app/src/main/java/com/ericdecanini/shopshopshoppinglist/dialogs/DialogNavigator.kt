package com.ericdecanini.shopshopshoppinglist.dialogs

interface DialogNavigator {

  fun displayAlertDialog(
      title: String? = null,
      message: String? = null,
      positiveText: String? = null,
      positiveOnClick: (() -> Unit)? = null,
      negativeText: String? = null,
      negativeOnClick: (() -> Unit)? = null,
      cancellable: Boolean = true
  )

}
