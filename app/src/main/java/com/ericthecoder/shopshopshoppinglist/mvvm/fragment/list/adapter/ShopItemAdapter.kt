package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.databinding.ListItemShopitemBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.library.extension.moveItem
import com.ericthecoder.shopshopshoppinglist.theme.Theme

class ShopItemAdapter(
    val items: MutableList<ShopItem>,
    private val theme: Theme,
    private val shopItemEventHandler: ShopItemEventHandler,
) : RecyclerView.Adapter<ShopItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemShopitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, theme)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, shopItemEventHandler)
    }

    fun replaceItems(newItems: List<ShopItem>) {
        items.clear()
        items.addAll(newItems)
    }

    fun moveItem(from: Int, to: Int) {
        items.moveItem(from, to)
        notifyItemMoved(from, to)
        shopItemEventHandler.onItemMoved(from, to)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        shopItemEventHandler.onItemRemoved(position)
    }

    class ViewHolder(
        private val binding: ListItemShopitemBinding,
        private val theme: Theme,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem, shopItemEventHandler: ShopItemEventHandler) = with(shopItem) {
            binding.setVariable(BR.viewstate, this)
            binding.setVariable(BR.handler, shopItemEventHandler)
            binding.setVariable(BR.quantity, binding.quantity)

            val context = binding.root.context
            binding.checkbox.buttonTintList = ColorStateList.valueOf(
                context.resources.getColor(theme.colorVariantRes, context.theme)
            )
        }
    }
}
