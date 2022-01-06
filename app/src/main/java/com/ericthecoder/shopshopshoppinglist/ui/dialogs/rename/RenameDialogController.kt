package com.ericthecoder.shopshopshoppinglist.ui.dialogs.rename

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.fragment.app.DialogFragment
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.rename.RenameDialogFragment.Companion.EXTRA_AUTOFILL_TEXT
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.rename.RenameDialogFragment.Companion.EXTRA_HEADER
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.rename.RenameDialogFragment.Companion.EXTRA_NEGATIVE_CLICK
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.rename.RenameDialogFragment.Companion.EXTRA_POSITIVE_CLICK

@Suppress("UNCHECKED_CAST")
class RenameDialogController(
  private val dialogFragment: DialogFragment,
  args: Bundle?
) {

  val header = args?.getString(EXTRA_HEADER)
  val autofillText = ObservableField(args?.getString(EXTRA_AUTOFILL_TEXT))

  val positiveOnClick: (String) -> Unit = {
    val positiveOnClick = args?.getSerializable(EXTRA_POSITIVE_CLICK) as? ((String) -> Unit)
    positiveOnClick?.invoke(it)
    dismiss()
  }

  val negativeOnClick = {
    val negativeOnClick = args?.getSerializable(EXTRA_NEGATIVE_CLICK) as? (() -> Unit)
    negativeOnClick?.invoke()
    dismiss()
  }

  private fun dismiss() {
    dialogFragment.dismiss()
  }
}
