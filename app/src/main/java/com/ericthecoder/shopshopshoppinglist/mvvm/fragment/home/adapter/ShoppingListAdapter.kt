package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.databinding.ListItemShoppinglistBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.google.android.material.color.MaterialColors
import com.google.android.material.elevation.SurfaceColors
import kotlinx.android.synthetic.main.list_item_shoppinglist.view.*

class ShoppingListAdapter(
    private val shoppingLists: List<ShoppingList>,
    private val onShoppingListClick: ShoppingListEventHandler,
) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemShoppinglistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.previewItems.adapter = PreviewItemsAdapter()
        binding.previewItems.layoutManager = LinearLayoutManager(parent.context)
        binding.tintCardBackground()
        return ViewHolder(binding)
    }

    private fun ListItemShoppinglistBinding.tintCardBackground() {
        val surface5 = SurfaceColors.SURFACE_5.getColor(root.context)
        val baseCardBackgroundColor = cardView.cardBackgroundColor.defaultColor
        val blendedColor = MaterialColors.layer(baseCardBackgroundColor, surface5, 1F)
        cardView.setCardBackgroundColor(blendedColor)
    }

    override fun getItemCount(): Int = shoppingLists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.setVariable(BR.viewstate, shoppingLists[position])
            binding.setVariable(BR.handler, onShoppingListClick)
            bind(shoppingLists[position])
        }
    }

    class ViewHolder(val binding: ListItemShoppinglistBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shoppingList: ShoppingList) {
            bindPreviewItems(shoppingList)
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun bindPreviewItems(shoppingList: ShoppingList) {
            (itemView.preview_items.adapter as? PreviewItemsAdapter)?.apply {
                if (shoppingList.items.isEmpty()) {
                    shoppingList.items.add(ShopItem("No items", 0, false))
                }
                replaceItems(shoppingList.items)
                notifyDataSetChanged()
            }
        }
    }
}
