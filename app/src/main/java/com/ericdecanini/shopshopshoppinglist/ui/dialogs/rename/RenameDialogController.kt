package com.ericdecanini.shopshopshoppinglist.ui.dialogs.rename

import androidx.databinding.ObservableField
import androidx.fragment.app.DialogFragment

class RenameDialogController(
    private val dialogFragment: DialogFragment,
    data: RenameDialogControllerData
) {

  val listTitleText = ObservableField(data.listTitleText)

  val positiveOnClick: (String) -> Unit = {
    data.positiveOnClick?.invoke(it)
    dismiss()
  }

  val negativeOnClick = {
    data.negativeOnClick?.invoke()
    dismiss()
  }

  private fun dismiss() {
    dialogFragment.dismiss()
  }
}
