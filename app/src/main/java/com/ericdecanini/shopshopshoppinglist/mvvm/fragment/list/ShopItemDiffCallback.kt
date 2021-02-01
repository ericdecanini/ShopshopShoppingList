package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import androidx.recyclerview.widget.DiffUtil
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem

class ShopItemDiffCallback(
    private val oldItems: List<ShopItem>,
    private val newItems: List<ShopItem>
) : DiffUtil.Callback() {

  override fun getOldListSize() = oldItems.size

  override fun getNewListSize() = newItems.size

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val same = oldItems[oldItemPosition].id == newItems[newItemPosition].id
    return same
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val same = oldItems[oldItemPosition] == newItems[newItemPosition]
    return same
  }

  override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
    return super.getChangePayload(oldItemPosition, newItemPosition)
  }
}
