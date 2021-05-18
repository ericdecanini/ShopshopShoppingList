package com.ericthecoder.shopshopshoppinglist.ui.dialogs.generic

import androidx.fragment.app.DialogFragment

class GenericDialogController(
    private val dialogFragment: DialogFragment,
    data: GenericDialogControllerData
) {

  val title = data.title
  val message = data.message

  val positiveText = data.positiveText
  val positiveOnClick = {
    data.positiveOnClick?.invoke()
    dismiss()
  }

  val negativeText = data.negativeText
  val negativeOnClick = {
    data.negativeOnClick?.invoke()
    dismiss()
  }

  private fun dismiss() {
    dialogFragment.dismiss()
  }
}
