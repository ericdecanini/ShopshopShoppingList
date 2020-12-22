package com.ericdecanini.shopshopshoppinglist.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ericdecanini.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.R
import kotlinx.android.synthetic.main.list_item_shopitem.view.*

class ShopItemAdapter(private val items: List<ShopItem>) : RecyclerView.Adapter<ShopItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.list_item_shopitem, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int  = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(shopItem: ShopItem) = with(shopItem) {
            itemView.checkbox.isChecked = shopItem.checked
            itemView.name.text = shopItem.name
            itemView.quantity.text = shopItem.quantity.toString()
        }
    }
}
