package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.R
import kotlinx.android.synthetic.main.list_item_previewitem.view.*
import kotlin.math.min

class PreviewItemsAdapter(
    private val items: List<ShopItem>
): RecyclerView.Adapter<PreviewItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_previewitem, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = min(3, items.size)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: ShopItem) {
            itemView.item.text = item.name
            itemView.item.isChecked = item.checked
        }
    }

}
