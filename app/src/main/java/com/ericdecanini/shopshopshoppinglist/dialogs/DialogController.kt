package com.ericdecanini.shopshopshoppinglist.dialogs

import androidx.fragment.app.DialogFragment

class DialogController(
    private val dialogFragment: DialogFragment,
    data: DialogControllerData
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
