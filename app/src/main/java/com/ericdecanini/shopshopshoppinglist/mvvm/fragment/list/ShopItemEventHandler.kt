package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.widget.CheckBox
import android.widget.EditText
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem

interface ShopItemEventHandler {

  fun onQuantityDown(shopItem: ShopItem)

  fun onQuantityUp(shopItem: ShopItem)

  fun onDeleteClick(shopItem: ShopItem)

  fun onCheckboxChecked(checkbox: CheckBox, shopItem: ShopItem)

  fun onNameChanged(editText: EditText, shopItem: ShopItem)

}