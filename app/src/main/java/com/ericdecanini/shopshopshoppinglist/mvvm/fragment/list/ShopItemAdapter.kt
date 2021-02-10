package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.ericdecanini.shopshopshoppinglist.BR
import com.ericdecanini.shopshopshoppinglist.databinding.ListItemShopitemBinding
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import kotlinx.android.synthetic.main.list_item_shopitem.view.*

class ShopItemAdapter(
    val items: MutableList<ShopItem>,
    private val shopItemEventHandler: ShopItemEventHandler
) : RecyclerView.Adapter<ShopItemAdapter.ViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemShopitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        viewBinderHelper.bind(holder.itemView as SwipeRevealLayout, item.id.toString())
        holder.bind(item, shopItemEventHandler)
    }

    fun saveStates(outState: Bundle) = viewBinderHelper.saveStates(outState)

    fun restoreStates(inState: Bundle) = viewBinderHelper.restoreStates(inState)

    fun replaceItems(newItems: List<ShopItem>) {
        items.clear()
        items.addAll(newItems)
    }

    class ViewHolder(private val binding: ListItemShopitemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem, shopItemEventHandler: ShopItemEventHandler) = with(shopItem) {
            binding.setVariable(BR.viewstate, this)
            binding.setVariable(BR.handler, shopItemEventHandler)
            binding.setVariable(BR.quantity, binding.quantity)

            binding.quantity.setOnClickListener {
                with(itemView.swipe_layout) {
                    if (isClosed) { open(true) } else { close(true) }
                }
            }
        }
    }
}
