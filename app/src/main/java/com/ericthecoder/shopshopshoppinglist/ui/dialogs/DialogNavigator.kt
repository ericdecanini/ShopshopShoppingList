package com.ericthecoder.shopshopshoppinglist.ui.dialogs

interface DialogNavigator {

  fun displayGenericDialog(
      title: String? = null,
      message: String? = null,
      positiveButton: Pair<String, (() -> Unit)?>? = null,
      negativeButton: Pair<String, (() -> Unit)?>? = null,
      cancellable: Boolean = true
  )

  fun displayRenameDialog(
      listTitle: String,
      positiveOnClick: ((String) -> Unit),
      negativeOnClick: (() -> Unit)? = null,
      cancellable: Boolean = true
  )
}
