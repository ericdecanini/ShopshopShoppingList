package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ericdecanini.shopshopshoppinglist.BR
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.databinding.ListItemPreviewitemBinding
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import kotlinx.android.synthetic.main.list_item_previewitem.view.*
import kotlin.math.min

class PreviewItemsAdapter: RecyclerView.Adapter<PreviewItemsAdapter.ViewHolder>() {

    private val items: MutableList<ShopItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemPreviewitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = min(3, items.size)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.setVariable(BR.viewstate, items[position])
    }

    fun replaceItems(items: List<ShopItem>) {
        this.items.clear()
        this.items.addAll(items)
    }

    class ViewHolder(val binding: ListItemPreviewitemBinding): RecyclerView.ViewHolder(binding.root)

}
