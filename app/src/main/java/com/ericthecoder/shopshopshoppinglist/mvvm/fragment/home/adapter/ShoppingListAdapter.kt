package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.databinding.ListItemShoppinglistBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import kotlinx.android.synthetic.main.list_item_shoppinglist.view.*

class ShoppingListAdapter(
    private val shoppingLists: List<ShoppingList>,
    private val onShoppingListClick: ShoppingListEventHandler
): RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemShoppinglistBinding.inflate(LayoutInflater.from(parent.context))
        binding.previewItems.adapter = PreviewItemsAdapter()
        binding.previewItems.layoutManager = LinearLayoutManager(parent.context)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = shoppingLists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.setVariable(BR.viewstate, shoppingLists[position])
            binding.setVariable(BR.handler, onShoppingListClick)
            bind(shoppingLists[position])
        }
    }

    class ViewHolder(val binding: ListItemShoppinglistBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(shoppingList: ShoppingList) {
            (itemView.preview_items.adapter as? PreviewItemsAdapter)?.apply {
                if (shoppingList.items.isEmpty()) {
                    shoppingList.items.add(ShopItem(-1, "No items", 0, false))
                }
                replaceItems(shoppingList.items)
                notifyDataSetChanged()
            }
        }
    }
}
