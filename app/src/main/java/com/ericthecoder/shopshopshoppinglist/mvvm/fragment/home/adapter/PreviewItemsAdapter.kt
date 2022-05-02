package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.ListItemPreviewitemBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.google.android.material.color.MaterialColors
import kotlin.math.min

class PreviewItemsAdapter : RecyclerView.Adapter<PreviewItemsAdapter.ViewHolder>() {

    private val items: MutableList<ShopItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemPreviewitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = min(4, items.size)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < 3)
            holder.binding.setVariable(BR.viewstate, items[position])
        else
            holder.binding.setVariable(BR.viewstate, null)

        holder.setThemeColors()
    }

    fun replaceItems(items: List<ShopItem>) {
        this.items.clear()
        this.items.addAll(items)
    }

    class ViewHolder(val binding: ListItemPreviewitemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setThemeColors() {
            val textColor = MaterialColors.getColor(binding.root, if (isDarkMode()) R.attr.colorOnPrimary else R.attr.colorOnSurface)
            binding.item.setTextColor(textColor)
            binding.continuationDots.setTextColor(textColor)
            TextViewCompat.setCompoundDrawableTintList(binding.item, ColorStateList.valueOf(textColor))
        }

        private fun isDarkMode() = binding.root.context.resources.getBoolean(R.bool.isDarkMode)
    }

}
