package com.ericdecanini.shopshopshoppinglist.ui.dialogs.generic

import android.content.Context
import com.ericdecanini.shopshopshoppinglist.R

class GenericDialogControllerData(context: Context) {

  var title = ""
  var message = ""
  var positiveText: String? = context.getString(R.string.ok)
  var positiveOnClick: (() -> Unit)? = null
  var negativeText: String? = null
  var negativeOnClick: (() -> Unit)? = null
  var cancellable = true

}
