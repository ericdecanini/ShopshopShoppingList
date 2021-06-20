package com.ericthecoder.shopshopshoppinglist.ui.dialogs.generic

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.generic.GenericDialogFragment.Companion.EXTRA_MESSAGE
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.generic.GenericDialogFragment.Companion.EXTRA_NEGATIVE_CLICK
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.generic.GenericDialogFragment.Companion.EXTRA_NEGATIVE_TEXT
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.generic.GenericDialogFragment.Companion.EXTRA_POSITIVE_CLICK
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.generic.GenericDialogFragment.Companion.EXTRA_POSITIVE_TEXT
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.generic.GenericDialogFragment.Companion.EXTRA_TITLE

@Suppress("UNCHECKED_CAST")
class GenericDialogController(
    private val dialogFragment: DialogFragment,
    args: Bundle?
) {

  val title = args?.getString(EXTRA_TITLE)

  val message = args?.getString(EXTRA_MESSAGE)

  val positiveText = args?.getString(EXTRA_POSITIVE_TEXT) ?: dialogFragment.context?.getString(R.string.ok)

  val positiveOnClick = {
    val positiveOnClick = args?.getSerializable(EXTRA_POSITIVE_CLICK) as (() -> Unit)?
    positiveOnClick?.invoke()
    dismiss()
  }

  val negativeText = args?.getString(EXTRA_NEGATIVE_TEXT)

  val negativeOnClick = {
    val negativeOnClick = args?.getSerializable(EXTRA_NEGATIVE_CLICK) as (() -> Unit)?
    negativeOnClick?.invoke()
    dismiss()
  }

  private fun dismiss() {
    dialogFragment.dismiss()
  }
}
