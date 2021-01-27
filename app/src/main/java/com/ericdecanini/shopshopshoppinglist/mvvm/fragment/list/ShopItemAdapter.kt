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
    private val items: List<ShopItem>,
    private val shopItemListListeners: ShopItemListListeners
) : RecyclerView.Adapter<ShopItemAdapter.ViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemShopitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        viewBinderHelper.bind(holder.itemView as SwipeRevealLayout, item.name)
        holder.bind(item, shopItemListListeners)
    }

    fun saveStates(outState: Bundle) = viewBinderHelper.saveStates(outState)

    fun restoreStates(inState: Bundle) = viewBinderHelper.restoreStates(inState)

    class ViewHolder(val binding: ListItemShopitemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem, shopItemListListeners: ShopItemListListeners) = with(shopItem) {
            binding.setVariable(BR.viewstate, this)
            binding.setVariable(BR.listeners, shopItemListListeners)

            binding.name.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus)
                    shopItemListListeners.onNameChangedListener(
                        NameChangedParams(shopItem, itemView.name.text.toString())
                    )
            }

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                shopItemListListeners.onCheckboxCheckedListener(
                    CheckboxCheckedParams(shopItem, isChecked)
                )
            }

            binding.quantity.setOnClickListener {
                with(itemView.swipe_layout) {
                    if (isClosed) { open(true) } else { close(true) }
                }
            }
        }
    }
}
