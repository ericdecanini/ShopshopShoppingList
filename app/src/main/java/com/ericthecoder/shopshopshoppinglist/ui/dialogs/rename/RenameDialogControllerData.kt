package com.ericthecoder.shopshopshoppinglist.ui.dialogs.rename

import android.content.Context

class RenameDialogControllerData(context: Context) {

  var listTitleText = ""
  var positiveOnClick: ((String) -> Unit)? = null
  var negativeOnClick: (() -> Unit)? = null
  var cancellable = true

}
