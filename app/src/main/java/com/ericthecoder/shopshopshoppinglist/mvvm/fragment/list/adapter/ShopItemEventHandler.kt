package com.ericthecoder.shopshopshoppinglist.adapter

import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem

interface ShopItemEventHandler {

  fun onQuantityDown(quantityView: TextView, shopItem: ShopItem)

  fun onQuantityUp(quantityView: TextView, shopItem: ShopItem)

  fun onDeleteClick(shopItem: ShopItem)

  fun onCheckboxChecked(checkbox: CheckBox, shopItem: ShopItem)

  fun onNameChanged(editText: EditText, shopItem: ShopItem)

  fun onFocusLost(swipeRevealLayout: SwipeRevealLayout)

}
