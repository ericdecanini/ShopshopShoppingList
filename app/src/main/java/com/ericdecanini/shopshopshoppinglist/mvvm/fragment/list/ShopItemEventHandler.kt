package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.view.View
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem

interface ShopItemEventHandler {

  fun onQuantityDown(shopItem: ShopItem)

  fun onQuantityUp(shopItem: ShopItem)

  fun onDeleteClick(shopItem: ShopItem)

  fun onCheckboxChecked(view: View, shopItem: ShopItem)

  fun onNameChanged(view: View, shopItem: ShopItem)

}
