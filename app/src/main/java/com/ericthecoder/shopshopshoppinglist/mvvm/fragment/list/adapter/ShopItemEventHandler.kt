package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.adapter

import android.widget.CheckBox
import android.widget.EditText
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem

interface ShopItemEventHandler {

  fun onDeleteClick(shopItem: ShopItem)

  fun onItemChecked(checkbox: CheckBox, shopItem: ShopItem)

  fun onNameChanged(editText: EditText, shopItem: ShopItem)

  fun onQuantityChanged(editText: EditText, shopItem: ShopItem)

}
