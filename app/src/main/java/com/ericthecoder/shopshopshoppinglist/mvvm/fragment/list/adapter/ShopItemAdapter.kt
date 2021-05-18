package com.ericthecoder.shopshopshoppinglist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.databinding.ListItemShopitemBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem

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

    fun replaceItems(newItems: List<ShopItem>) {
        items.clear()
        items.addAll(newItems)
    }

    class ViewHolder(private val binding: ListItemShopitemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem, shopItemEventHandler: ShopItemEventHandler) = with(shopItem) {
            binding.setVariable(BR.viewstate, this)
            binding.setVariable(BR.handler, shopItemEventHandler)
            binding.setVariable(BR.quantity, binding.quantity)

            with(binding.swipeLayout) {
                close(false)

                binding.quantity.setOnClickListener {
                    if (isClosed) {
                        open(true)
                        requestFocus()
                    } else {
                        close(true)
                        clearFocus()
                    }
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) { close(true) }
                }
            }
        }
    }
}
