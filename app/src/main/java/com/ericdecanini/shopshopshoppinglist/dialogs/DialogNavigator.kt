package com.ericdecanini.shopshopshoppinglist.dialogs

interface DialogNavigator {

  fun displayGenericDialog(
      title: String? = null,
      message: String? = null,
      positiveText: String? = null,
      positiveOnClick: (() -> Unit)? = null,
      negativeText: String? = null,
      negativeOnClick: (() -> Unit)? = null,
      cancellable: Boolean = true
  )

  fun displayRenameDialog(
      listTitle: String,
      positiveOnClick: ((String) -> Unit),
      negativeOnClick: (() -> Unit)? = null,
      cancellable: Boolean = true
  )

}
