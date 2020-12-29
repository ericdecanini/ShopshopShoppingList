package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ericdecanini.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.R
import kotlinx.android.synthetic.main.list_item_shoppinglist.view.*

class ShoppingListAdapter(
    private val shoppingLists: List<ShoppingList>,
    private val onShoppingListClick: (ShoppingList, NavController) -> Unit,
    private val navController: NavController
): RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_shoppinglist, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = shoppingLists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shoppingLists[position], onShoppingListClick, navController)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(
            shoppingList: ShoppingList,
            onShoppingListClick: (ShoppingList, NavController) -> Unit,
            navController: NavController
        ) {
            itemView.title.text = shoppingList.title
            itemView.preview_items.adapter = PreviewItemsAdapter(shoppingList.items)
            itemView.preview_items.layoutManager = LinearLayoutManager(itemView.context)

            itemView.setOnClickListener { onShoppingListClick(shoppingList, navController) }
        }
    }

}