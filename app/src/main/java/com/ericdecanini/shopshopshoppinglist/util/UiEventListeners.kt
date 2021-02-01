package com.ericdecanini.shopshopshoppinglist.util

import android.widget.CheckBox
import android.widget.EditText

class UiEventListeners {

  interface OnFocusLostListener {
    fun onFocusLost(view: EditText)
  }

  interface OnCheckChangedListener {
    fun onCheckChanged(view: CheckBox)
  }

}
