package com.ericthecoder.shopshopshoppinglist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem

class ShopItemDiffCallback(
    private val oldItems: List<ShopItem>,
    private val newItems: List<ShopItem>
) : DiffUtil.Callback() {

  override fun getOldListSize() = oldItems.size

  override fun getNewListSize() = newItems.size

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldItems[oldItemPosition].id == newItems[newItemPosition].id
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldItems[oldItemPosition] == newItems[newItemPosition]
  }
}
