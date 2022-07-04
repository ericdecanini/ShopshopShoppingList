package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.ListItemShopitemBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.library.extension.moveItem

class ShopItemAdapter(
    val items: MutableList<ShopItem>,
    private val shopItemEventHandler: ShopItemEventHandler,
) : RecyclerView.Adapter<ShopItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemShopitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        setCheckboxStyle(binding)
        return ViewHolder(binding)
    }

    private fun setCheckboxStyle(binding: ListItemShopitemBinding) {
        val context = binding.root.context

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val checkStyle = prefs.getString(context.getString(R.string.checkbox_style_key),
            context.resources.getStringArray(R.array.checkbox_style_values).firstOrNull())
        val defaultCheckbox = binding.checkbox.buttonDrawable

        binding.checkbox.buttonDrawable = if (checkStyle == context.getString(R.string.checkbox_style_value_circular)) {
            ContextCompat.getDrawable(context, R.drawable.rounded_checkbox)
        } else {
            defaultCheckbox
        }
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

    class ViewHolder(private val binding: ListItemShopitemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem, shopItemEventHandler: ShopItemEventHandler) = with(shopItem) {
            binding.setVariable(BR.viewstate, this)
            binding.setVariable(BR.handler, shopItemEventHandler)
            binding.setVariable(BR.quantity, binding.quantity)
        }
    }
}
