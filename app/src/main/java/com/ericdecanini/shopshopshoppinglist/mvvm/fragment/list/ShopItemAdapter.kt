package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.ericdecanini.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.R
import kotlinx.android.synthetic.main.list_item_shopitem.view.*

class ShopItemAdapter(
    private val items: List<ShopItem>,
    private val onShopItemUpdate: (ShopItem, ShopItem) -> Unit
) : RecyclerView.Adapter<ShopItemAdapter.ViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.list_item_shopitem, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int  = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        viewBinderHelper.bind(holder.itemView as SwipeRevealLayout, item.name)

        holder.bind(item, onShopItemUpdate)
    }

    fun saveStates(outState: Bundle) = viewBinderHelper.saveStates(outState)

    fun restoreStates(inState: Bundle) = viewBinderHelper.restoreStates(inState)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            shopItem: ShopItem,
            onShopItemUpdate: (ShopItem, ShopItem) -> Unit
        ) = with(shopItem) {
            itemView.checkbox.isChecked = shopItem.checked
            itemView.name.setText(shopItem.name)
            itemView.quantity.text = shopItem.quantity.toString()

            itemView.name.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus)
                    onShopItemUpdate(shopItem, shopItem.withName(itemView.name.text.toString()))
            }

            itemView.quantity.setOnClickListener {
                with(itemView.swipe_layout) {
                    if (isClosed) { open(true) } else { close(true) }
                }
            }

            itemView.quantity_down.setOnClickListener {
                onShopItemUpdate(shopItem, shopItem.withQuantity(quantity - 1))
            }

            itemView.quantity_up.setOnClickListener {
                onShopItemUpdate(shopItem, shopItem.withQuantity(quantity + 1))
            }

            itemView.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onShopItemUpdate(shopItem, shopItem.withChecked(isChecked))
            }
        }
    }
}
