package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem

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
