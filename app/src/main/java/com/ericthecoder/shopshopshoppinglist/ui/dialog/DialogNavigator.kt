package com.ericthecoder.shopshopshoppinglist.ui.dialog

interface DialogNavigator {

  fun displayGenericDialog(
      title: String? = null,
      message: String? = null,
      positiveButton: Pair<String, (() -> Unit)?>? = null,
      negativeButton: Pair<String, (() -> Unit)?>? = null,
      cancellable: Boolean = true
  )

  fun displayRenameDialog(
      header: String,
      autofillText: String,
      positiveOnClick: ((String) -> Unit),
      negativeOnClick: (() -> Unit)? = null,
      cancellable: Boolean = true
  )
}
