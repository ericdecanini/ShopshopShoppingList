package com.ericdecanini.shopshopshoppinglist.dialogs

import android.content.Context
import com.ericdecanini.shopshopshoppinglist.R

class DialogControllerData(context: Context) {

  var title = ""
  var message = ""
  var positiveText: String? = context.getString(R.string.ok)
  var positiveOnClick: (() -> Unit)? = null
  var negativeText: String? = context.getString(R.string.cancel)
  var negativeOnClick: (() -> Unit)? = null
  var cancellable = true

}
