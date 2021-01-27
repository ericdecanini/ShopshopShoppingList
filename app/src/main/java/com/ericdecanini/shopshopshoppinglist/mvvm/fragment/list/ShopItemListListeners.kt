package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.util.ItemClickListener

data class ShopItemListListeners(
    val onQuantityDownClickListener: ItemClickListener<ShopItem>,
    val onQuantityUpClickListener: ItemClickListener<ShopItem>,
    val onDeleteClickListener: ItemClickListener<ShopItem>,
    val onCheckboxCheckedListener: (CheckboxCheckedParams) -> Unit,
    val onNameChangedListener: (NameChangedParams) -> Unit
)

data class CheckboxCheckedParams(
    val shopItem: ShopItem,
    val checked: Boolean
)

data class NameChangedParams(
    val shopItem: ShopItem,
    val newName: String
)
