package com.ericthecoder.shopshopshoppinglist.util.listeners

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.chauthai.swipereveallayout.SwipeRevealLayout

class UiEventListeners {

  interface OnEditTextEventListener {
    fun onEvent(view: EditText)
  }

  interface OnSwipeRevealLayoutEvent {
    fun onEvent(view: SwipeRevealLayout)
  }

  interface OnCheckChangedListener {
    fun onCheckChanged(view: CheckBox)
  }

  interface OnFocusGainedListener {
    fun onFocusGained(view: View)
  }

}
