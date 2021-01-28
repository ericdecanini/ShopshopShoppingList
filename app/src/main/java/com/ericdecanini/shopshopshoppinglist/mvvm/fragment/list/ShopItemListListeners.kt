package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.util.FocusChangeListener
import com.ericdecanini.shopshopshoppinglist.util.ItemCheckedListener
import com.ericdecanini.shopshopshoppinglist.util.ItemClickListener

data class ShopItemListListeners(
    val onQuantityDownClickListener: ItemClickListener<ShopItem>,
    val onQuantityUpClickListener: ItemClickListener<ShopItem>,
    val onDeleteClickListener: ItemClickListener<ShopItem>,
    val onCheckboxCheckedListener: ItemCheckedListener<ShopItem>,
    val onNameChangedListener: FocusChangeListener<ShopItem>
)
